/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jd.watcher.spi;

import com.jd.watcher.domain.Event;

import java.util.List;

/**
 * Dept：将chanel中消息直接传送到外部服务器，或者本地保存
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public interface TransportService {
    /**
     * 将消息直接进行发送
     * @param event
     */
    public void sendEvent(Event event);

    /**
     *
     * @param events 消息集合
     */
    public void sendEvent(List<Event> events);
}
