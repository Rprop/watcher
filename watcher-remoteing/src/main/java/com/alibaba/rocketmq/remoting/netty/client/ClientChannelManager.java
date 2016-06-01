package com.alibaba.rocketmq.remoting.netty.client;

import com.alibaba.rocketmq.remoting.common.RemoteHelper;
import com.alibaba.rocketmq.remoting.common.RemoteUtil;
import com.alibaba.rocketmq.remoting.exception.RemoteConnectException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/3
 * Version:1.0
 */
public class ClientChannelManager {
    private static final Logger logger = LoggerFactory.getLogger(ClientChannelManager.class);
    private final Bootstrap bootstrap;
    private final NettyClientConfig config;
    /* key:addr value:通道包装类 */
    private final ConcurrentHashMap<String, ChannelWrapper> channelTables = new ConcurrentHashMap<String, ChannelWrapper>();
    private final Lock lockChannelTables = new ReentrantLock();

    // Name server相关
    private final AtomicReference<List<String>> serverAddressList;

    private final AtomicReference<String> serverAddressCached = new AtomicReference<String>();
    private final Lock cachedLock = new ReentrantLock();

    private final AtomicInteger namesrvIndex = new AtomicInteger(initValueIndex());
    private static final long LockTimeoutMillis = 3000;

    ClientChannelManager(NettyClientConfig config, Bootstrap bootstrap, AtomicReference<List<String>> serverAddressList) {
        this.config = config;
        this.bootstrap = bootstrap;
        this.serverAddressList = serverAddressList;
    }

    private static int initValueIndex() {
        Random r = new Random();

        return Math.abs(r.nextInt() % 999) % 999;
    }

    Channel getOrCreateChannel(final String address) throws InterruptedException, RemoteConnectException {
        Channel channel;
        if (null == address) {
            logger.info("从列表中随机获取通道。");
            channel = getOrCreateChannel();
        } else {
//            logger.info("创建连接通道");
            channel = this.createChannel(address);
        }
        if (channel != null && channel.isActive()) {
            return channel;
        } else {
            this.closeChannel(address, channel);
            throw new RemoteConnectException(address);
        }
    }


    /**
     * 获取默认通道
     *
     * @return
     * @throws InterruptedException
     */
    private Channel getOrCreateChannel() throws InterruptedException {
        String addr = this.serverAddressCached.get();
        if (addr != null) {
            ChannelWrapper cw = this.channelTables.get(addr);
            if (cw != null && cw.isOK()) {
                return cw.getChannel();
            }
        }
        if (this.cachedLock.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)) {
            try {
                addr = this.serverAddressCached.get();
                if (addr != null) {
                    ChannelWrapper cw = this.channelTables.get(addr);
                    if (cw != null && cw.isOK()) {
                        return cw.getChannel();
                    }
                }

                final List<String> addrList = this.serverAddressList.get();
                if (addrList != null && !addrList.isEmpty()) {
                    for (int i = 0; i < addrList.size(); i++) {
                        int index = this.namesrvIndex.incrementAndGet();
                        index = Math.abs(index);
                        index = index % addrList.size();
                        String newAddr = addrList.get(index);

                        this.serverAddressCached.set(newAddr);
                        Channel channelNew = this.createChannel(newAddr);
                        if (channelNew != null)
                            return channelNew;
                    }
                }
            } catch (Exception e) {
                logger.error("getOrCreateChannel: create name server channel exception", e);
            } finally {
                this.cachedLock.unlock();
            }
        } else {
            logger.warn("getOrCreateChannel: try to lock name server, but timeout, {}ms", LockTimeoutMillis);
        }

        return null;
    }

    /**
     * @param addr
     * @return
     * @throws InterruptedException
     */
    private Channel createChannel(final String addr) throws InterruptedException {
        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isOK()) {
            return cw.getChannel();
        }

        // 进入临界区后，不能有阻塞操作，网络连接采用异步方式
        if (this.lockChannelTables.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)) {
            try {
                boolean createNewConnection = false;
                cw = this.channelTables.get(addr);
                if (cw != null) {
                    // channel正常
                    if (cw.isOK()) {
                        return cw.getChannel();
                    }
                    // 正在连接，退出锁等待
                    else if (!cw.getChannelFuture().isDone()) {
                        createNewConnection = false;
                    }
                    // 说明连接不成功
                    else {
                        this.channelTables.remove(addr);
                        createNewConnection = true;
                    }
                }
                // ChannelWrapper不存在
                else {
                    createNewConnection = true;
                }

                if (createNewConnection) {
                    ChannelFuture channelFuture = this.bootstrap.connect(RemoteHelper.string2SocketAddress(addr));
                    logger.info("createChannel: begin to connect remote host[{}] asynchronously", addr);
                    cw = new ChannelWrapper(channelFuture);
                    this.channelTables.put(addr, cw);
                }
            } catch (Exception e) {
                logger.error("createChannel: create channel exception", e);
            } finally {
                this.lockChannelTables.unlock();
            }
        } else {
            logger.warn("createChannel: try to lock channel table, but timeout, {}ms", LockTimeoutMillis);
        }

        if (cw != null) {
            ChannelFuture channelFuture = cw.getChannelFuture();
            if (channelFuture.awaitUninterruptibly(this.config.getConnectTimeoutMillis())) {
                if (cw.isOK()) {
                    logger.info("connect remote host[{}] success, {}", addr, channelFuture.toString());
                    return cw.getChannel();
                } else {
                    logger.warn("connect remote host[" + addr + "] failed, " + channelFuture.toString(), channelFuture.cause());
                }
            } else {
                logger.warn("connect remote host[{}] timeout {}ms, {}", addr, this.config.getConnectTimeoutMillis(), channelFuture.toString());
            }
        }

        return null;
    }

    /**
     * 通道不可用或者连接断开，进行关闭
     *
     * @param addr
     * @param channel
     */
    public void closeChannel(final String addr, final Channel channel) {
        if (null == channel) {
            return;
        }

        final String addrRemote = null == addr ? RemoteHelper.parseChannelRemoteAddr(channel) : addr;
        try {
            if (this.lockChannelTables.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)) {
                try {
                    boolean removeItemFromTable = true;
                    final ChannelWrapper prevCW = this.channelTables.get(addrRemote);

                    logger.info("begin close the channel[{}] Found: {}", addrRemote, (prevCW != null));

                    if (null == prevCW) {
                        logger.info("the channel[{}] has been removed from the channel table before", addrRemote);
                        removeItemFromTable = false;
                    } else if (prevCW.getChannel() != channel) {
                        logger.info("the channel[{}] has been closed before, and has been created again, nothing to do.", addrRemote);
                        removeItemFromTable = false;
                    }

                    if (removeItemFromTable) {
                        this.channelTables.remove(addrRemote);
                        logger.info("closeChannel: the channel[{}] was removed from channel table", addrRemote);
                    }

                    RemoteUtil.closeChannel(channel);
                } catch (Exception e) {
                    logger.error("closeChannel: close the channel exception", e);
                } finally {
                    this.lockChannelTables.unlock();
                }
            } else {
                logger.warn("closeChannel: try to lock channel table, but timeout, {}ms", LockTimeoutMillis);
            }
        } catch (InterruptedException e) {
            logger.error("closeChannel exception", e);
        }
    }


    public void closeChannel(final Channel channel) {
        if (null == channel)
            return;

        try {
            if (this.lockChannelTables.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)) {
                try {
                    boolean removeItemFromTable = true;
                    ChannelWrapper prevCW = null;
                    String addrRemote = null;
                    for (String key : channelTables.keySet()) {
                        ChannelWrapper prev = this.channelTables.get(key);
                        if (prev.getChannel() != null) {
                            if (prev.getChannel() == channel) {
                                prevCW = prev;
                                addrRemote = key;
                                break;
                            }
                        }
                    }

                    if (null == prevCW) {
                        logger.info(
                                "eventCloseChannel: the channel[{}] has been removed from the channel table before",
                                addrRemote);
                        removeItemFromTable = false;
                    }

                    if (removeItemFromTable) {
                        this.channelTables.remove(addrRemote);
                        logger.info("closeChannel: the channel[{}] was removed from channel table", addrRemote);
                        RemoteUtil.closeChannel(channel);
                    }
                } catch (Exception e) {
                    logger.error("closeChannel: close the channel exception", e);
                } finally {
                    this.lockChannelTables.unlock();
                }
            } else {
                logger.warn("closeChannel: try to lock channel table, but timeout, {}ms", LockTimeoutMillis);
            }
        } catch (InterruptedException e) {
            logger.error("closeChannel exception", e);
        }
    }

    /**
     * 连接信息关闭
     */
    public void shutdown() {
        for (ChannelWrapper cw : this.channelTables.values()) {
            this.closeChannel(null, cw.getChannel());
        }
        this.channelTables.clear();
    }
}
