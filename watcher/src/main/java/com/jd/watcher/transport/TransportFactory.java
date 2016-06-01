package com.jd.watcher.transport;

import com.jd.watcher.transport.tofile.TransportToLog;
import com.jd.watcher.spi.TransportService;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public class TransportFactory {

    public static TransportService getService() {
        return new TransportToLog();
//        return new TransportEventToRemote();
    }
}
