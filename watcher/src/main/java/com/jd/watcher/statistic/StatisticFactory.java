package com.jd.watcher.statistic;

import com.jd.watcher.spi.StatisticService;
import com.jd.watcher.spi.TransportService;
import com.jd.watcher.transport.tofile.TransportToLog;

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
