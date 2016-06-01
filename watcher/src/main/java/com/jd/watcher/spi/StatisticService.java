package com.jd.watcher.spi;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/17
 * Version:1.0
 */
public interface StatisticService {
    /**
     * 队列放满直接丢弃的数据
     */
    public void sendSlowDiscard();
}
