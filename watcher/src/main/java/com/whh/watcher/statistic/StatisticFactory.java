package com.whh.watcher.statistic;

import com.whh.watcher.spi.StatisticService;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/17
 * Version:1.0
 */
public class StatisticFactory {
    public static StatisticService getService() {
        return new StatisticServiceImpl();
//        return new TransportEventToRemote();
    }
}
