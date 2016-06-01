package com.jd.watcher.domain;

import com.alibaba.fastjson.JSON;
import com.jd.watcher.domain.enums.EventTypeEnum;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class AlarmEvent extends Event {
    private String data;

    public AlarmEvent() {
    }

    public  AlarmEvent(String key, String data) {
        this.event = EventTypeEnum.alarm;
        this.key = key;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toJsonString() {
        return "A" +JSON.toJSONString(this);
    }
}
