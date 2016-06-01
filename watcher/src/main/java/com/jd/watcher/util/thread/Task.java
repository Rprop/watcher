package com.jd.watcher.util.thread;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/13
 * Version:1.0
 */
public interface Task extends Runnable {
    public String getName();

    public void shutdown();
}
