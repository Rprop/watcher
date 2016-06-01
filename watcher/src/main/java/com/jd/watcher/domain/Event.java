package com.jd.watcher.domain;

import com.jd.watcher.channel.EventChannel;
import com.jd.watcher.domain.enums.EventTypeEnum;
import com.jd.watcher.config.ConfigFactory;
import com.jd.watcher.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public abstract class Event implements Serializable {
    private Logger logger = LoggerFactory.getLogger(Event.class);
    private String id;//事件ID
    protected EventTypeEnum event;//事件类型
    private String app;//应用名称
    protected String key;//业务主建
    protected Long time;//事件开始时间
    private String hostIP;//机器IP

    public Event() {
        this.time = MilliSecondTimer.currentTimeMillis();
        this.app = ConfigFactory.getSystemConfig().getAppName();
        this.hostIP = ConfigFactory.getSystemConfig().getIpAddress();
        this.id = MessageIdUtils.getNextMessId();
    }

    /**
     * 事件处理完成，将事件交由事件管理内下进后续处理
     */
    public void end() {
        try {
            EventChannel.collectEvent(this);
        } catch (Exception e) {
            logger.error("发送消息异常", e);
        }
    }

    public String getId() {
        return id;
    }

    public EventTypeEnum getEvent() {
        return event;
    }

    public String getApp() {
        return app;
    }

    public String getKey() {
        return key;
    }

    public Long getTime() {
        return time;
    }


    public String getHostIp() {
        return hostIP;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", event=" + event +
                ", app='" + app + '\'' +
                ", key='" + key + '\'' +
                ", time=" + time +
                ", hostIP='" + hostIP + '\'' +
                '}';
    }

    public abstract String toJsonString();

//    public boolean canMerge() {
//        return false;
//    }
//
//    public String mergeKey() {
//        if (!canMerge()) {
//            throw new RuntimeException("事件不可以合并");
//        } else {
//            return this.key;
//        }
//    }
//
//    public void merge(Event event){
//
//    }
}
