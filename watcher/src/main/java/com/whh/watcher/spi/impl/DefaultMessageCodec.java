package com.whh.watcher.spi.impl;

import com.alibaba.fastjson.JSON;
import com.whh.watcher.domain.*;
import com.whh.watcher.spi.MessageCodec;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Dept：消息处理
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public class DefaultMessageCodec implements MessageCodec {
    private static Logger logger = LoggerFactory.getLogger(DefaultMessageCodec.class);

    @Override
    public List<Event> decode(byte[] buf) {
        List<Event> result = new ArrayList<Event>();
        String tempStr = new String(buf);
        JSON.parseObject(buf, String.class);
        List<String> strList = JSON.parseArray(tempStr, String.class);
        for (String str : strList) {
            result.add(parseToEvent(str));
        }
        return result;
    }

    private Event parseToEvent(String str) {

        if (str.startsWith("A")) {
            return JSON.parseObject(str.substring(1), AlarmEvent.class);
        }else if(str.startsWith("C")){
            return JSON.parseObject(str.substring(1), CountEvent.class);
        }else if(str.startsWith("W")){
            return JSON.parseObject(str.substring(1), WatchEvent.class);
        }else if(str.startsWith("J")){
            return JSON.parseObject(str.substring(1), JvmEvent.class);
        }
        return null;
    }

    @Override
    public void decode(ByteBuf buf, List<Event> events) {
        events = this.decode(buf.array());
    }


    @Override
    public byte[] encode(List<Event> events) {
        List<String> list = new ArrayList<String>();
        for (Event event : events) {
            list.add(event.toJsonString());
        }
        String jsonStr = JSON.toJSONString(list);
        logger.info("真正发送的消息:{}", jsonStr);

        return jsonStr.getBytes();
    }
}
