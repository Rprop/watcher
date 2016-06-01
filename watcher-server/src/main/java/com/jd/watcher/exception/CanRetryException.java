package com.jd.watcher.exception;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public class CanRetryException extends WatcherException {
    public CanRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanRetryException(String message) {
        super(message);
    }

    public CanRetryException(Throwable cause) {
        super(cause);
    }
}
