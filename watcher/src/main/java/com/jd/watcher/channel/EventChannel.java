package com.jd.watcher.channel;

import com.jd.watcher.config.ConfigFactory;
import com.jd.watcher.domain.Event;
import com.jd.watcher.statistic.StatisticFactory;
import com.jd.watcher.transport.TransportFactory;
import com.jd.watcher.spi.TransportService;
import com.jd.watcher.util.thread.ServiceRunnable;
import com.jd.watcher.util.thread.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Dept：消息都经过管道处理后，进行相应输出
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class EventChannel {
    private static Logger logger = LoggerFactory.getLogger(EventChannel.class);
    private static TransportService sendEventService;
    private static ArrayBlockingQueue<Event> waitTransportQueue;
    private final static EventChannel channel = new EventChannel();
    private static EventMergeManager eventMergeManager;

    private EventChannel() {
        try {
            logger.info("-------channel init-------");
            sendEventService = TransportFactory.getService();
            waitTransportQueue = new ArrayBlockingQueue<Event>(ConfigFactory.getSystemConfig().getEventBufferedMaxSize());
            eventMergeManager = new EventMergeManager();
            Threads.forGroup("messageTransport").start(new MessageTransport());
        } catch (Exception e) {
            logger.error("init monitor client error. please check config", e);
        }
    }

    /**
     * 消息发送(如果发送过快，直接将消息丢弃)
     *
     * @param event
     */
    public static void collectEvent(Event event) {
        if (event instanceof MergeAble) {
            eventMergeManager.mergerEvent(event);
        } else {
            collectImmediately(event);
        }
    }

    /**
     * 立即将消息加入发送通道，不做合并操作
     *
     * @param event
     */
    public static void collectImmediately(Event event) {
        boolean result = waitTransportQueue.offer(event);
        if (!result) {
            StatisticFactory.getService().sendSlowDiscard();
        }
    }


    class MessageTransport extends ServiceRunnable {
        private List<Event> batch = new ArrayList<Event>();

        public void run() {
            logger.info(this.getServiceName() + " service started");
            while (!this.isStopped()) {
                try {
                    Event event = waitTransportQueue.take();
                    if (waitTransportQueue.size() > 0) {
                        batch.add(event);
                        waitTransportQueue.drainTo(batch, ConfigFactory.getSystemConfig().getEventBatchSendMaxSize());
                        sendEventService.sendEvent(batch);
                        batch.clear();
                    } else {
                        sendEventService.sendEvent(event);
                    }
                } catch (InterruptedException e) {
                    logger.error("发送监控日志异常：", e);
                }
            }
            logger.info(this.getServiceName() + " service end");
        }

        @Override
        public String getServiceName() {
            return this.getClass().getSimpleName();
        }
    }
}
