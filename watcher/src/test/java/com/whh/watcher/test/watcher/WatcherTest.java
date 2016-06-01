package com.whh.watcher.test.watcher;

import com.whh.watcher.Watcher;
import com.whh.watcher.domain.WatchEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public class WatcherTest {
    public static void main(String[] args) throws InterruptedException {
        WatchEvent event = Watcher.watchStart("key", "service");
        try {
            Watcher.watchForCount("count_key");
            Watcher.watchForSum("sum_key", 124.123);
            Watcher.watchAlarm("alarm_key", "系统出问题了，有人管没有啊！");
            new Thread(){
                @Override
                public void run() {
                    WatchEvent event = Watcher.watchStart("key", "service");
                    WatchEvent event1 = Watcher.watchStart("key", "service");
                    Watcher.watchEnd(event1);
                    Watcher.watchEnd(event);
                    System.out.println("thread 父---:"+event.toJsonString());
                    System.out.println("thread 子---:"+event1.toJsonString());
                }
            }.start();

            WatchEvent event1 = Watcher.watchStart("key", "service");
            Watcher.watchEnd(event1);
            System.out.println(event1.toJsonString());
        } catch (Exception e) {
            event.exception(e);
        } finally {
            Watcher.watchEnd(event);
            System.out.println(event.toJsonString());
        }
        Thread.sleep(5000);
    }

    /**
     * 测试多线程发送
     */
    private static void multiSend() throws InterruptedException {
        Long time = System.currentTimeMillis();
        int totalThread = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(totalThread);
        final CountDownLatch latch = new CountDownLatch(totalThread);
        for (int i = 0; i < totalThread; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            WatchEvent event = Watcher.watchStart("key", "service");
                            try {
                                Watcher.watchForCount("count_key");
                                Watcher.watchForSum("sum_key", 124.123);
                                Watcher.watchAlarm("alarm_key", "系统出问题了，有人管没有啊！" + i);
                            } catch (Exception e) {
                                event.exception(e);
                            } finally {
                                event.end();
                            }
                        }
                    } finally {
                        latch.countDown();
                    }

                }
            });
        }
        latch.await();
        System.out.println(System.currentTimeMillis() - time);
    }
}
