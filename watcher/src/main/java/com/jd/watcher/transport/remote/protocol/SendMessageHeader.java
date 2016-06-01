package com.jd.watcher.transport.remote.protocol;

import com.alibaba.rocketmq.remoting.CommandCustomHeader;
import com.alibaba.rocketmq.remoting.exception.RemoteCommandException;

/**
 * Dept：普通发送消息头信息
 * User:wanghanghang
 * Date:2016/5/4
 * Version:1.0
 */
public class SendMessageHeader implements CommandCustomHeader {
    @Override
    public void checkFields() throws RemoteCommandException {
    }

    private String messageType;
    private String appName;
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
