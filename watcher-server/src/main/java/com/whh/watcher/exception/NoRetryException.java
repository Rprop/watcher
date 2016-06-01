package com.whh.watcher.exception;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public class NoRetryException extends WatcherException {
    public NoRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRetryException(String message) {
        super(message);
    }

    public NoRetryException(Throwable cause) {
        super(cause);
    }
}
