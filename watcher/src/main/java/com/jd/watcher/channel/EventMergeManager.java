package com.jd.watcher.channel;

import com.jd.watcher.config.ConfigFactory;
import com.jd.watcher.domain.Event;
import com.jd.watcher.util.cache.LruCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/17
 * Version:1.0
 */
public class EventMergeManager {
    private static Logger logger = LoggerFactory.getLogger(EventChannel.class);
    private final LruCache<String, Event> cached;
    private final long COUNT_PERIOD = ConfigFactory.getSystemConfig().getEventSurvivePeriod();
    private final ReentrantLock lock = new ReentrantLock(false);

    EventMergeManager() {
        Thread shutdownThread = new Thread() {
            @Override
            public void run() {
                addToChannel(cached.snapshot().values());
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                        logger.info("system will Terminated in ({})秒 ", i);
                    }catch (Exception e){}

                }
            }
        };
        shutdownThread.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(shutdownThread);
        logger.info("ShutdownHook add for flush cached data");
        cached = new LruCache<String, Event>(ConfigFactory.getSystemConfig().getEventCachedMergeMaxSize()) {
            @Override
            protected void entryRemoved(boolean evicted, String key, Event oldValue, Event newValue) {
                if (!evicted) {// if the removal was caused by a {@link #put} or {@link #remove}.
                    addToChannel(oldValue);
                }
            }
        };
        startTimer();
    }

    private void addToChannel(Event... events) {
        for (Event event : events) {
            EventChannel.collectImmediately(event);
        }
    }

    private void addToChannel(Collection<Event> events) {
        for (Event event : events) {
            EventChannel.collectImmediately(event);
        }
    }

    /**
     * 收集统计数量服务
     *
     * @param event
     */
    public void mergerEvent(Event event) {
        lock.lock();
        try {
            MergeAble newValue = (MergeAble) event;
            Event oldValue = cached.get(newValue.mergeKey());

            if (oldValue != null) {
                ((MergeAble) oldValue).merge(event);
            } else {
                cached.put(newValue.mergeKey(), event);
            }
        } finally {
            lock.unlock();
        }
    }

    private void startTimer() {
        //定时获取jvm运行时信息
        Timer timer = new Timer("Watcher_Merge_Event_Thread", true);
        long lastTimePoint = (new Date().getTime() / COUNT_PERIOD) * COUNT_PERIOD;
        Date firstWriteTime = new Date(lastTimePoint + COUNT_PERIOD);

        logger.info("WatcherMergeEventTimer will starting in ({})ms,period is:{}", firstWriteTime, COUNT_PERIOD);
        //定时推送JVM运行信息
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                lock.lock();
                try {
                    addToChannel(cached.snapshot().values());
                    cached.evictAll();
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
            }
        }, firstWriteTime, COUNT_PERIOD);
    }
}
