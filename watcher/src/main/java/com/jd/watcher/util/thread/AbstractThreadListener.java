package com.jd.watcher.util.thread;

import java.util.concurrent.ExecutorService;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/13
 * Version:1.0
 */
public class AbstractThreadListener implements  ThreadListener {
    @Override
    public void onThreadGroupCreated(ThreadGroup group, String name) {
        // to be override
    }

    @Override
    public void onThreadPoolCreated(ExecutorService pool, String name) {
        // to be override
    }

    @Override
    public void onThreadStarting(Thread thread, String name) {
        // to be override
    }

    @Override
    public void onThreadStopping(Thread thread, String name) {
        // to be override
    }

    @Override
    public boolean onUncaughtException(Thread thread, Throwable e) {
        // to be override
        return false;
    }
}
