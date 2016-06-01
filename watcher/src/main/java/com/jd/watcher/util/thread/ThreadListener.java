package com.jd.watcher.util.thread;

import java.util.concurrent.ExecutorService;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/13
 * Version:1.0
 */
public interface ThreadListener {
    public void onThreadGroupCreated(ThreadGroup group, String name);

    /**
     * Triggered when a thread pool (ExecutorService) has been created.
     *
     * @param pool
     *           thread pool
     * @param pattern
     *           thread pool name pattern
     */
    public void onThreadPoolCreated(ExecutorService pool, String pattern);

    /**
     * Triggered when a thread is starting.
     *
     * @param thread
     *           thread which is starting
     * @param name
     *           thread name
     */
    public void onThreadStarting(Thread thread, String name);

    public void onThreadStopping(Thread thread, String name);

    /**
     * Triggered when an uncaught exception thrown from within a thread.
     *
     * @param thread
     *           thread which has an uncaught exception thrown
     * @param e
     *           the exception uncaught
     * @return true means the exception is handled, it will be not handled again other listeners, false otherwise.
     */
    public boolean onUncaughtException(Thread thread, Throwable e);
}
