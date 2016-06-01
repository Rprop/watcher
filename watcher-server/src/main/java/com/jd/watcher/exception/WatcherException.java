package com.jd.watcher.exception;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public class WatcherException extends Exception {
    public WatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public WatcherException(String message) {
        super(message);
    }

    public WatcherException(Throwable cause) {
        super(cause);
    }
}
