package com.whh.watcher.receiver;


import com.whh.watcher.domain.Event;

public interface MessageHandler {
    /**
     * 处理消息接口
     * @param message
     */
    public void handle(Event message);
}
