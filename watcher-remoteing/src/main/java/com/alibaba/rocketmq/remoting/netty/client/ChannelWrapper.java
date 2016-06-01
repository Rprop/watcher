package com.alibaba.rocketmq.remoting.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/3
 * Version:1.0
 */
public class ChannelWrapper {
    private final ChannelFuture channelFuture;

    public ChannelWrapper(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public boolean isOK() {
        return (this.channelFuture.channel() != null && this.channelFuture.channel().isActive());
    }


    public boolean isWriteable() {
        return this.channelFuture.channel().isWritable();
    }


     Channel getChannel() {
        return this.channelFuture.channel();
    }


    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }
}
