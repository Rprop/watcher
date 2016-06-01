package com.alibaba.rocketmq.remoting.netty.event;

import com.alibaba.rocketmq.remoting.ChannelEventListener;
import com.alibaba.rocketmq.remoting.common.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/3
 * Version:1.0
 */
public class NettyEventWorker extends ServiceThread {
    private static final Logger logger = LoggerFactory.getLogger(NettyEventWorker.class);
    private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
    private final int MaxSize = 10000;
    private ChannelEventListener eventListener;

    /**
     * 添加业务通知接口
     *
     * @param eventListener
     */
    public NettyEventWorker addEventListener(ChannelEventListener eventListener) {
        this.eventListener = eventListener;
        return this;
    }

    /**
     * 将网网络事件包装，异步通知业务系统接口
     *
     * @param event
     */
    public void putNettyEvent(final NettyEvent event) {
        if (this.eventQueue.size() <= MaxSize) {
            this.eventQueue.add(event);
        } else {
            logger.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(),
                    event.toString());
        }
    }

    @Override
    public void run() {
        logger.info(this.getServiceName() + " service started");
        final ChannelEventListener listener = this.eventListener;

        while (!this.isStoped()) {
            try {
                NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                if (event != null && listener != null) {
                    switch (event.getType()) {
                        case IDLE:
                            listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CLOSE:
                            listener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CONNECT:
                            listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                            break;
                        case EXCEPTION:
                            listener.onChannelException(event.getRemoteAddr(), event.getChannel());
                            break;
                        default:
                            break;

                    }
                }
            } catch (Exception e) {
                logger.warn(this.getServiceName() + " service has exception. ", e);
            }
        }

        logger.info(this.getServiceName() + " service end");
    }


    @Override
    public String getServiceName() {
        return NettyEventWorker.class.getSimpleName();
    }
}
