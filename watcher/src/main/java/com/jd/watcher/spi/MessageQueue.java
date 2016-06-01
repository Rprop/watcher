package com.jd.watcher.spi;

import com.jd.watcher.domain.Event;

public interface MessageQueue {
    /**
     * 所有的事件都加入分析队列
     *
     * @param event 事件
     * @return
     */
    public boolean offer(Event event);

    /**
     * 将事件加入到队列，进行部分丢弃
     *
     * @param event       事件
     * @param sampleRatio 丢弃概率 在0到1之间
     * @return
     */
    public boolean offer(Event event, double sampleRatio);

    public Event peek();

    public Event poll();

    // the current size of the queue
    public int size();
}
