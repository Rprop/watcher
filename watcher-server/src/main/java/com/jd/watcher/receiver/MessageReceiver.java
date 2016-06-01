package com.jd.watcher.receiver;

import com.alibaba.rocketmq.remoting.netty.NettyRequestProcessor;
import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;
import com.jd.watcher.CatConstants;
import com.jd.watcher.domain.Event;
import com.jd.watcher.spi.MessageCodec;
import com.jd.watcher.statistic.ServerStatisticManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Dept：服务端消息接收处理
 * User:wanghanghang
 * Date:2016/5/6
 * Version:1.0
 */
@Service("messageReceiver")
public class MessageReceiver implements NettyRequestProcessor {
    @Resource
    private MessageHandler handler;
    @Resource
    private ServerStatisticManager statisticManager;
    @Resource
    private MessageCodec messageCodec;
    private volatile long processCount;

    @Override
    public RemoteCommand processRequest(ChannelHandlerContext ctx, RemoteCommand request) throws Exception {
        byte[] data = request.getBody();
        List<Event> events = messageCodec.decode(data);
        for (Event event : events) {
            if (event != null) {
                handler.handle(event);
                statisticManager.addMessageTotal(CatConstants.SUCCESS_COUNT);
                statisticManager.addMessageTotal(event.getApp(), 1);
            } else {
                statisticManager.addMessageTotalLoss(CatConstants.ERROR_COUNT);
                statisticManager.addMessageTotalLoss(event.getApp(), 1);
            }
        }
        return request;
    }
}
