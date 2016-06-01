package com.whh.ump.profiler.util;

import com.jd.ump.log4j.Category;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * log缓存和异步写文件
 *
 * @author chenzehong
 */
@Deprecated
public class MessagesStore {
    // 缓存最大值为256K条
    private final static int MAX_MSG_PER_CATEGORY = 1024 * 256;
    private final static int BATCH_LOG_SIZE = 128;
    // 换行符
    private final static String LINE_SEP = System.getProperty("line.separator");

    private final ConcurrentLinkedQueue<Object> messages;
    private final Category category;

    // 缓存条数计数器
    private final AtomicInteger counter;

    public MessagesStore(Category category) {
        // 初始化日志缓存队列
        messages = new ConcurrentLinkedQueue<Object>();
        counter = new AtomicInteger(0);
        this.category = category;

        int bufferSize = 1024 * 8;
        if ("tpLogger".equals(category.getName()) || "bizLogger".equals(category.getName())) {
            bufferSize = 1024 * 32;
        }

        // 初始化写日志到文件线程
        Thread writeThread = new Thread(new WriteLog2File(bufferSize), "UMP-WriteLog2FileThread-" + category.getName());
        writeThread.setDaemon(true);
        writeThread.start();
    }

    public void storeMsg(Object message) {
        // 超过缓存限制就丢掉日志。
        // 注：理论上是不可能发生的，除非磁盘出现了问题，写不进去了，出现该情况原生log4j方式也写不进去。
        if (counter.get() < MAX_MSG_PER_CATEGORY) {
            messages.offer(message);
            counter.incrementAndGet();
        } else {
        }
    }

    /**
     * 写日志到文件的线程
     *
     * @author chenzehong
     */
    class WriteLog2File implements Runnable {
        StringBuilder logBuffer;

        // 读取缓存计数
        private int pollCount;

        public WriteLog2File(int bufferSize) {
            pollCount = 0;
            logBuffer = new StringBuilder(bufferSize);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    pollCount++;
                    Object msg = messages.poll();

                    if (msg != null) {
                        // 从缓存队列取到了日志消息就对缓存条数计数器-1
                        counter.decrementAndGet();
                        logBuffer.append(msg.toString()).append(LINE_SEP);
                    } else {
                        // 从缓存队列没有获取到日志消息就休眠1毫秒
                        try {
                            Thread.sleep(1L);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }

                    if (pollCount >= BATCH_LOG_SIZE) {
                        // 每BATCH_LOG_SIZE条日志写一次磁盘
                        // reset pollCount
                        pollCount = 0;
                        writeLogNow();
                    }
                } catch (Throwable ex) {
                    // 出现问题休眠1毫秒
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        }

        /**
         * 写日志到磁盘
         */
        private void writeLogNow() {
            int logLength = logBuffer.length();

            if (logLength > 0) {
                try {
                    category.infoReal(logBuffer.toString());
                } finally {
                    // reset logs
                    logBuffer.setLength(0);
                }
            }
        }
    }
}