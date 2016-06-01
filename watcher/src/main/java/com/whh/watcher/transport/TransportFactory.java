package com.whh.watcher.transport;

import com.whh.watcher.transport.tofile.TransportToLog;
import com.whh.watcher.spi.TransportService;

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
