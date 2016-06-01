/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.rocketmq.remoting.netty;

import com.alibaba.rocketmq.remoting.netty.event.NettyEvent;
import com.alibaba.rocketmq.remoting.netty.event.NettyEventWorker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.remoting.InvokeCallback;
import com.alibaba.rocketmq.remoting.RPCHook;
import com.alibaba.rocketmq.remoting.common.Pair;
import com.alibaba.rocketmq.remoting.common.RemoteHelper;
import com.alibaba.rocketmq.remoting.common.SemaphoreReleaseOnlyOnce;
import com.alibaba.rocketmq.remoting.exception.RemoteSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemoteTimeoutException;
import com.alibaba.rocketmq.remoting.exception.RemoteTooMuchRequestException;
import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;
import com.alibaba.rocketmq.remoting.protocol.RemoteSysResponseCode;


/**
 * Server与Client公用抽象类
 *
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2013-7-13
 */
public abstract class NettyRemoteAbstract {
    private static final Logger logger = LoggerFactory.getLogger(RemoteHelper.RemotingLogName);
    // 信号量，Oneway情况会使用，防止本地Netty缓存请求过多
    protected final Semaphore semaphoreOneway;
    // 信号量，异步调用情况会使用，防止本地Netty缓存请求过多
    protected final Semaphore semaphoreAsync;
    // 缓存所有对外请求
    protected final ConcurrentHashMap<Integer /* opaque */, ResponseFuture> responseTable =
            new ConcurrentHashMap<Integer, ResponseFuture>(256);

    // 默认请求代码处理器
    protected Pair<NettyRequestProcessor, ExecutorService> defaultRequestProcessor;

    // 注册的各个RPC处理器
    protected final HashMap<Integer/* request code */, Pair<NettyRequestProcessor, ExecutorService>> processorTable =
            new HashMap<Integer, Pair<NettyRequestProcessor, ExecutorService>>(64);

    protected final NettyEventWorker nettyEventWorker = new NettyEventWorker();

    public abstract RPCHook getRPCHook();

    public void putNettyEvent(final NettyEvent event) {
        this.nettyEventWorker.putNettyEvent(event);
    }

    public NettyRemoteAbstract(final int permitsOneway, final int permitsAsync) {
        this.semaphoreOneway = new Semaphore(permitsOneway, true);
        this.semaphoreAsync = new Semaphore(permitsAsync, true);
    }


    public void processRequestCommand(final ChannelHandlerContext ctx, final RemoteCommand cmd) {
        final Pair<NettyRequestProcessor, ExecutorService> matched = this.processorTable.get(cmd.getCode());
        final Pair<NettyRequestProcessor, ExecutorService> pair = null == matched ? this.defaultRequestProcessor : matched;

        if (pair != null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {

                        RPCHook rpcHook = NettyRemoteAbstract.this.getRPCHook();
                        if (rpcHook != null) {
                            rpcHook.doBeforeRequest(RemoteHelper.parseChannelRemoteAddr(ctx.channel()), cmd);
                        }

                        final RemoteCommand response = pair.getObject1().processRequest(ctx, cmd);
                        if (rpcHook != null) {
                            rpcHook.doAfterResponse(RemoteHelper.parseChannelRemoteAddr(ctx.channel()),
                                    cmd, response);
                        }

                        if (!cmd.isOnewayRPC()) {
                            if (response != null) {
                                response.setOpaque(cmd.getOpaque());
                                response.markResponseType();
                                try {
                                    ctx.writeAndFlush(response);
                                } catch (Throwable e) {
                                    logger.error("process request over, but response failed", e);
                                    logger.error(cmd.toString());
                                    logger.error(response.toString());
                                }
                            } else {
                                // 收到请求，但是没有返回应答，可能是processRequest中进行了应答，忽略这种情况
                            }
                        }
                    } catch (Throwable e) {
                        logger.error("process request exception", e);
                        logger.error(cmd.toString());

                        if (!cmd.isOnewayRPC()) {
                            final RemoteCommand response =
                                    RemoteCommand.createResponseCommand(
                                            RemoteSysResponseCode.SYSTEM_ERROR,//
                                            RemoteHelper.exceptionSimpleDesc(e));
                            response.setOpaque(cmd.getOpaque());
                            ctx.writeAndFlush(response);
                        }
                    }
                }
            };

            try {
                // 这里需要做流控，要求线程池对应的队列必须是有大小限制的
                pair.getObject2().submit(run);
            } catch (RejectedExecutionException e) {
                // 每个线程10s打印一次
                if ((System.currentTimeMillis() % 10000) == 0) {
                    logger.warn(RemoteHelper.parseChannelRemoteAddr(ctx.channel()) //
                            + ", too many requests and system thread pool busy, RejectedExecutionException " //
                            + pair.getObject2().toString() //
                            + " request code: " + cmd.getCode());
                }

                if (!cmd.isOnewayRPC()) {
                    final RemoteCommand response =
                            RemoteCommand.createResponseCommand(RemoteSysResponseCode.SYSTEM_BUSY,
                                    "too many requests and system thread pool busy, please try another server");
                    response.setOpaque(cmd.getOpaque());
                    ctx.writeAndFlush(response);
                }
            }
        } else {
            String error = " request type " + cmd.getCode() + " not supported";
            final RemoteCommand response =
                    RemoteCommand.createResponseCommand(RemoteSysResponseCode.REQUEST_CODE_NOT_SUPPORTED,
                            error);
            response.setOpaque(cmd.getOpaque());
            ctx.writeAndFlush(response);
            logger.error(RemoteHelper.parseChannelRemoteAddr(ctx.channel()) + error);
        }
    }


    public void processResponseCommand(ChannelHandlerContext ctx, RemoteCommand cmd) {
        final ResponseFuture responseFuture = responseTable.get(cmd.getOpaque());
        if (responseFuture != null) {
            responseFuture.setResponseCommand(cmd);
            responseFuture.release();
            responseTable.remove(cmd.getOpaque());

            if (responseFuture.getInvokeCallback() != null) {
                boolean runInThisThread = false;
                ExecutorService executor = this.getCallbackExecutor();
                if (executor != null) {
                    try {
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    responseFuture.executeInvokeCallback();
                                } catch (Throwable e) {
                                    logger.warn("excute callback in executor exception, and callback throw", e);
                                }
                            }
                        });
                    } catch (Exception e) {
                        runInThisThread = true;
                        logger.warn("excute callback in executor exception, maybe executor busy", e);
                    }
                } else {
                    runInThisThread = true;
                }

                if (runInThisThread) {
                    try {
                        responseFuture.executeInvokeCallback();
                    } catch (Throwable e) {
                        logger.warn("executeInvokeCallback Exception", e);
                    }
                }
            } else {
                responseFuture.putResponse(cmd);
            }
        } else {
            logger.warn("receive response, but not matched any request, "
                    + RemoteHelper.parseChannelRemoteAddr(ctx.channel()));
            logger.warn(cmd.toString());
        }

    }


    public void processMessageReceived(ChannelHandlerContext ctx, RemoteCommand msg) throws Exception {
        final RemoteCommand cmd = msg;
        if (cmd != null) {
            switch (cmd.getType()) {
                case REQUEST_COMMAND:
                    processRequestCommand(ctx, cmd);
                    break;
                case RESPONSE_COMMAND:
                    processResponseCommand(ctx, cmd);
                    break;
                default:
                    break;
            }
        }
    }


    abstract public ExecutorService getCallbackExecutor();


    public void scanResponseTable() {
        Iterator<Entry<Integer, ResponseFuture>> it = this.responseTable.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ResponseFuture> next = it.next();
            ResponseFuture rep = next.getValue();

            if ((rep.getBeginTimestamp() + rep.getTimeoutMillis() + 1000) <= System.currentTimeMillis()) {
                it.remove();
                try {
                    rep.executeInvokeCallback();
                } catch (Throwable e) {
                    logger.warn("scanResponseTable, operationComplete Exception", e);
                } finally {
                    rep.release();
                }

                logger.warn("remove timeout request, " + rep);
            }
        }
    }


    public RemoteCommand invokeSyncImpl(final Channel channel, final RemoteCommand request,
                                        final long timeoutMillis) throws InterruptedException, RemoteSendRequestException,
            RemoteTimeoutException {
        try {
            final ResponseFuture responseFuture =
                    new ResponseFuture(request.getOpaque(), timeoutMillis, null, null);
            this.responseTable.put(request.getOpaque(), responseFuture);
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        responseFuture.setSendRequestOK(true);
                        return;
                    } else {
                        responseFuture.setSendRequestOK(false);
                    }

                    responseTable.remove(request.getOpaque());
                    responseFuture.setCause(f.cause());
                    responseFuture.putResponse(null);
                    logger.warn("send a request command to channel <" + channel.remoteAddress() + "> failed.");
                    logger.warn(request.toString());
                }
            });

            RemoteCommand responseCommand = responseFuture.waitResponse(timeoutMillis);
            if (null == responseCommand) {
                if (responseFuture.isSendRequestOK()) {
                    throw new RemoteTimeoutException(RemoteHelper.parseChannelRemoteAddr(channel),
                            timeoutMillis, responseFuture.getCause());
                } else {
                    throw new RemoteSendRequestException(RemoteHelper.parseChannelRemoteAddr(channel),
                            responseFuture.getCause());
                }
            }

            return responseCommand;
        } finally {
            this.responseTable.remove(request.getOpaque());
        }
    }


    public void invokeAsyncImpl(final Channel channel, final RemoteCommand request,
                                final long timeoutMillis, final InvokeCallback invokeCallback) throws InterruptedException,
            RemoteTooMuchRequestException, RemoteTimeoutException, RemoteSendRequestException {
        boolean acquired = this.semaphoreAsync.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
        if (acquired) {
            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreAsync);

            final ResponseFuture responseFuture = new ResponseFuture(request.getOpaque(), timeoutMillis, invokeCallback, once);
            this.responseTable.put(request.getOpaque(), responseFuture);
            try {
                channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        if (f.isSuccess()) {
                            responseFuture.setSendRequestOK(true);
                            return;
                        } else {
                            responseFuture.setSendRequestOK(false);
                        }
                        responseFuture.putResponse(null);
                        responseTable.remove(request.getOpaque());
                        try {
                            responseFuture.executeInvokeCallback();
                        } catch (Throwable e) {
                            logger.warn("excute callback in writeAndFlush addListener, and callback throw", e);
                        } finally {
                            responseFuture.release();
                        }

                        logger.warn("send a request command to channel <{}> failed.",
                                RemoteHelper.parseChannelRemoteAddr(channel));
                        logger.warn(request.toString());
                    }
                });
            } catch (Exception e) {
                responseFuture.release();
                logger.warn(
                        "send a request command to channel <" + RemoteHelper.parseChannelRemoteAddr(channel)
                                + "> Exception", e);
                throw new RemoteSendRequestException(RemoteHelper.parseChannelRemoteAddr(channel), e);
            }
        } else {
            if (timeoutMillis <= 0) {
                throw new RemoteTooMuchRequestException("invokeAsyncImpl invoke too fast");
            } else {
                String info =
                        String
                                .format(
                                        "invokeAsyncImpl tryAcquire semaphore timeout, %dms, waiting thread nums: %d semaphoreAsyncValue: %d", //
                                        timeoutMillis,//
                                        this.semaphoreAsync.getQueueLength(),//
                                        this.semaphoreAsync.availablePermits()//
                                );
                logger.warn(info);
                logger.warn(request.toString());
                throw new RemoteTimeoutException(info);
            }
        }
    }


    public void invokeOnewayImpl(final Channel channel, final RemoteCommand request,
                                 final long timeoutMillis) throws InterruptedException, RemoteTooMuchRequestException,
            RemoteTimeoutException, RemoteSendRequestException {
        request.markOnewayRPC();
        boolean acquired = this.semaphoreOneway.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
        if (acquired) {
            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreOneway);
            try {
                channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        once.release();
                        if (!f.isSuccess()) {
                            logger.warn("send a request command to channel <" + channel.remoteAddress()
                                    + "> failed.");
                            logger.warn(request.toString());
                        }
                    }
                });
            } catch (Exception e) {
                once.release();
                logger.warn("write send a request command to channel <" + channel.remoteAddress() + "> failed.");
                throw new RemoteSendRequestException(RemoteHelper.parseChannelRemoteAddr(channel), e);
            }
        } else {
            if (timeoutMillis <= 0) {
                throw new RemoteTooMuchRequestException("invokeOnewayImpl invoke too fast");
            } else {
                String info =
                        String
                                .format(
                                        "invokeOnewayImpl tryAcquire semaphore timeout, %dms, waiting thread nums: %d semaphoreAsyncValue: %d", //
                                        timeoutMillis,//
                                        this.semaphoreAsync.getQueueLength(),//
                                        this.semaphoreAsync.availablePermits()//
                                );
                logger.warn(info);
                logger.warn(request.toString());
                throw new RemoteTimeoutException(info);
            }
        }
    }
}
