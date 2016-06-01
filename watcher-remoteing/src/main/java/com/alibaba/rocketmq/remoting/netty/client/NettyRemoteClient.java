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
package com.alibaba.rocketmq.remoting.netty.client;

import com.alibaba.rocketmq.remoting.netty.*;
import com.alibaba.rocketmq.remoting.netty.event.NettyEvent;
import com.alibaba.rocketmq.remoting.netty.event.NettyEventType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.remoting.ChannelEventListener;
import com.alibaba.rocketmq.remoting.InvokeCallback;
import com.alibaba.rocketmq.remoting.RPCHook;
import com.alibaba.rocketmq.remoting.RemoteClient;
import com.alibaba.rocketmq.remoting.common.Pair;
import com.alibaba.rocketmq.remoting.common.RemoteHelper;
import com.alibaba.rocketmq.remoting.exception.RemoteConnectException;
import com.alibaba.rocketmq.remoting.exception.RemoteSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemoteTimeoutException;
import com.alibaba.rocketmq.remoting.exception.RemoteTooMuchRequestException;
import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;


/**
 * Remoting客户端实现
 *
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2013-7-13
 */
public class NettyRemoteClient extends NettyRemoteAbstract implements RemoteClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRemoteClient.class);

    private static final long LockTimeoutMillis = 3000;

    private final NettyClientConfig config;
    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup eventLoopGroupWorker;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    private final AtomicReference<List<String>> serverAddrList = new AtomicReference<List<String>>();
//    private final Lock lockChannelTables = new ReentrantLock();
//    /* key:addr value:通道包装类 */
//    private final ConcurrentHashMap<String, ChannelWrapper> channelTables = new ConcurrentHashMap<String, ChannelWrapper>();

    // 定时器
    private final Timer timer = new Timer("ClientHouseKeepingService", true);

//    // Name server相关
//    private final AtomicReference<List<String>> namesrvAddrList = new AtomicReference<List<String>>();
//    private final AtomicReference<String> namesrvAddrChoosed = new AtomicReference<String>();
//    private final AtomicInteger namesrvIndex = new AtomicInteger(initValueIndex());
//    private final Lock lockNamesrvChannel = new ReentrantLock();

    private final ClientChannelManager channelManager;
    // 处理Callback应答器
    private final ExecutorService publicExecutor;

    private final ChannelEventListener channelEventListener;

    private RPCHook rpcHook;

    class NettyClientHandler extends SimpleChannelInboundHandler<RemoteCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemoteCommand msg) throws Exception {
            processMessageReceived(ctx, msg);

        }
    }

    class NettyConnectManageHandler extends ChannelDuplexHandler {
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
                            SocketAddress localAddress, ChannelPromise promise) throws Exception {
            final String local = localAddress == null ? "UNKNOW" : localAddress.toString();
            final String remote = remoteAddress == null ? "UNKNOW" : remoteAddress.toString();
            logger.info("netty client pipeline: connect  {} => {}", local, remote);
            super.connect(ctx, remoteAddress, localAddress, promise);

            if (NettyRemoteClient.this.channelEventListener != null) {
                NettyRemoteClient.this.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, remoteAddress
                        .toString(), ctx.channel()));
            }
        }


        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            final String remoteAddress = RemoteHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("netty client pipeline: disconnect {}", remoteAddress);
            channelManager.closeChannel(ctx.channel());
            super.disconnect(ctx, promise);

            if (NettyRemoteClient.this.channelEventListener != null) {
                NettyRemoteClient.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress
                        .toString(), ctx.channel()));
            }
        }


        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            final String remoteAddress = RemoteHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("netty client pipeline: close {}", remoteAddress);
            channelManager.closeChannel(ctx.channel());
            super.close(ctx, promise);

            if (NettyRemoteClient.this.channelEventListener != null) {
                NettyRemoteClient.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress
                        .toString(), ctx.channel()));
            }
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final String remoteAddress = RemoteHelper.parseChannelRemoteAddr(ctx.channel());
            logger.warn("netty client pipeline: exception caught {}", remoteAddress);
            logger.warn("netty client pipeline: exception caught exception.", cause);
            channelManager.closeChannel(ctx.channel());
            if (NettyRemoteClient.this.channelEventListener != null) {
                NettyRemoteClient.this.putNettyEvent(new NettyEvent(NettyEventType.EXCEPTION, remoteAddress
                        .toString(), ctx.channel()));
            }
        }


        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.ALL_IDLE)) {
                    final String remoteAddress = RemoteHelper.parseChannelRemoteAddr(ctx.channel());
                    logger.warn("netty client pipeline: idle exception [{}]", remoteAddress);
                    channelManager.closeChannel(ctx.channel());
                    if (NettyRemoteClient.this.channelEventListener != null) {
                        NettyRemoteClient.this.putNettyEvent(new NettyEvent(NettyEventType.IDLE,
                                remoteAddress.toString(), ctx.channel()));
                    }
                }
            }

            ctx.fireUserEventTriggered(evt);
        }
    }


    private static int initValueIndex() {
        Random r = new Random();

        return Math.abs(r.nextInt() % 999) % 999;
    }


    public NettyRemoteClient(final NettyClientConfig nettyClientConfig) {
        this(nettyClientConfig, null);
    }


    public NettyRemoteClient(final NettyClientConfig clientConfig,//
                             final ChannelEventListener channelEventListener) {
        super(clientConfig.getClientOnewaySemaphoreValue(), clientConfig.getClientAsyncSemaphoreValue());
        this.config = clientConfig;
        this.channelEventListener = channelEventListener;

        channelManager = new ClientChannelManager(clientConfig, bootstrap, serverAddrList);

        int publicThreadNums = clientConfig.getClientCallbackExecutorThreads();
        if (publicThreadNums <= 0) {
            publicThreadNums = 4;
        }

        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);


            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "NettyClientPublicExecutor_" + this.threadIndex.incrementAndGet());
            }
        });

        this.eventLoopGroupWorker = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);


            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyClientSelector_%d",
                        this.threadIndex.incrementAndGet()));
            }
        });
    }


    @Override
    public void start() {
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(//
                config.getClientWorkerThreads(), //
                new ThreadFactory() {

                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientWorkerThread_" + this.threadIndex.incrementAndGet());
                    }
                });

        Bootstrap handler = this.bootstrap.group(this.eventLoopGroupWorker).channel(NioSocketChannel.class)//
                //
                .option(ChannelOption.TCP_NODELAY, true)
                        //
                .option(ChannelOption.SO_KEEPALIVE, false)
                        //
                .option(ChannelOption.SO_SNDBUF, config.getClientSocketSndBufSize())
                        //
                .option(ChannelOption.SO_RCVBUF, config.getClientSocketRcvBufSize())
                        //
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(//
                                defaultEventExecutorGroup, //
                                new NettyEncoder(), //
                                new NettyDecoder(), //
                                new IdleStateHandler(0, 0, config.getClientChannelMaxIdleTimeSeconds()),//
                                new NettyConnectManageHandler(), //
                                new NettyClientHandler());
                    }
                });

        this.timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    NettyRemoteClient.this.scanResponseTable();
                } catch (Exception e) {
                    logger.error("scanResponseTable exception", e);
                }
            }
        }, 1000 * 3, 1000);

        if (this.channelEventListener != null) {
            this.nettyEventWorker.addEventListener(channelEventListener).start();
        }
    }


    @Override
    public void shutdown() {
        try {
            this.timer.cancel();

            channelManager.shutdown();//连接通道关闭

            this.eventLoopGroupWorker.shutdownGracefully();

            if (this.nettyEventWorker != null) {
                this.nettyEventWorker.shutdown();
            }

            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error("NettyRemotingClient shutdown exception, ", e);
        }

        if (this.publicExecutor != null) {
            try {
                this.publicExecutor.shutdown();
            } catch (Exception e) {
                logger.error("NettyRemotingServer shutdown exception, ", e);
            }
        }
    }

    @Override
    public void registerProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executor) {
        ExecutorService executorThis = executor;
        if (null == executor) {
            executorThis = this.publicExecutor;
        }
        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<NettyRequestProcessor, ExecutorService>(processor, executorThis);
        this.processorTable.put(requestCode, pair);
    }


    @Override
    public RemoteCommand invokeSync(String addr, final RemoteCommand request, long timeoutMillis)
            throws InterruptedException, RemoteConnectException, RemoteSendRequestException,
            RemoteTimeoutException {
        final Channel channel = channelManager.getOrCreateChannel(addr);
        try {
            if (this.rpcHook != null) {
                this.rpcHook.doBeforeRequest(addr, request);
            }
            RemoteCommand response = this.invokeSyncImpl(channel, request, timeoutMillis);
            if (this.rpcHook != null) {
                this.rpcHook.doAfterResponse(RemoteHelper.parseChannelRemoteAddr(channel),
                        request, response);
            }
            return response;
        } catch (RemoteSendRequestException e) {
            logger.warn("invokeSync: send request exception, so close the channel[{}]", addr);
            channelManager.closeChannel(addr, channel);
            throw e;
        } catch (RemoteTimeoutException e) {
            logger.warn("invokeSync: wait response timeout exception, the channel[{}]", addr);
            throw e;
        }
    }


    @Override
    public void invokeAsync(String addr, RemoteCommand request, long timeoutMillis,
                            InvokeCallback invokeCallback) throws InterruptedException, RemoteConnectException,
            RemoteTooMuchRequestException, RemoteTimeoutException, RemoteSendRequestException {
        final Channel channel = channelManager.getOrCreateChannel(addr);
        try {
            if (this.rpcHook != null) {
                this.rpcHook.doBeforeRequest(addr, request);
            }
            this.invokeAsyncImpl(channel, request, timeoutMillis, invokeCallback);
        } catch (RemoteSendRequestException e) {
            logger.warn("invokeAsync: send request exception, so close the channel[{}]", addr);
            channelManager.closeChannel(addr, channel);
            throw e;
        }
    }


    @Override
    public void invokeOneway(String addr, RemoteCommand request, long timeoutMillis)
            throws InterruptedException, RemoteConnectException, RemoteTooMuchRequestException,
            RemoteTimeoutException, RemoteSendRequestException {
        final Channel channel = channelManager.getOrCreateChannel(addr);
        try {
            if (this.rpcHook != null) {
                this.rpcHook.doBeforeRequest(addr, request);
            }
            this.invokeOnewayImpl(channel, request, timeoutMillis);
        } catch (RemoteSendRequestException e) {
            logger.warn("invokeOneway: send request exception, so close the channel[{}]", addr);
            channelManager.closeChannel(addr, channel);
            throw e;
        }
    }


    @Override
    public ExecutorService getCallbackExecutor() {
        return this.publicExecutor;
    }


    @Override
    public void updateServerAddressList(List<String> addrs) {
        List<String> old = this.serverAddrList.get();
        boolean update = false;

        if (!addrs.isEmpty()) {
            if (null == old) {
                update = true;
            } else if (addrs.size() != old.size()) {
                update = true;
            } else {
                for (int i = 0; i < addrs.size() && !update; i++) {
                    if (!old.contains(addrs.get(i))) {
                        update = true;
                    }
                }
            }

            if (update) {
                Collections.shuffle(addrs);
                this.serverAddrList.set(addrs);
            }
        }
    }

    @Override
    public List<String> getServerAddressList() {
        return this.serverAddrList.get();
    }


    public RPCHook getRpcHook() {
        return rpcHook;
    }


    @Override
    public void registerRPCHook(RPCHook rpcHook) {
        this.rpcHook = rpcHook;
    }


    @Override
    public RPCHook getRPCHook() {
        return this.rpcHook;
    }

//    @Override
//    public boolean isChannelWriteAble(String addr) {
//        ChannelWrapper cw = channelManager.getOrCreateChannel(addr);
//        if (cw != null && cw.isOK()) {
//            return cw.isWriteable();
//        }
//        return true;
//    }
}
