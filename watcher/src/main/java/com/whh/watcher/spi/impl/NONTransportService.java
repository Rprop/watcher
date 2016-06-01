/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.whh.watcher.spi.impl;

import com.whh.watcher.domain.Event;
import com.whh.watcher.spi.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Dept：默认发送消息实现
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public class NONTransportService implements TransportService {
    private Logger logger = LoggerFactory.getLogger(NONTransportService.class);

    @Override
    public void sendEvent(Event event) {
        logger.info("------发送消息------" + event.toString());
    }

    @Override
    public void sendEvent(List<Event> events) {
        logger.info("------批量发送消息------" + events.toString());
    }
}
