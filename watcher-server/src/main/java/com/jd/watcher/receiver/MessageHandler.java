package com.jd.watcher.receiver;


import com.jd.watcher.domain.Event;

public interface MessageHandler {
    /**
     * 处理消息接口
     * @param message
     */
    public void handle(Event message);
}
