package com.jd.watcher.spi.impl;


import com.jd.watcher.domain.Event;
import com.jd.watcher.spi.MessageQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultMessageQueue implements MessageQueue {
    private BlockingQueue<Event> queue;
    private AtomicInteger counter = new AtomicInteger();

    public DefaultMessageQueue(int size) {
        queue = new LinkedBlockingQueue<Event>(size);
    }

    @Override
    public boolean offer(Event event) {
        return queue.offer(event);
    }

    @Override
    public boolean offer(Event event, double sampleRatio) {
        if (sampleRatio < 1.0 && sampleRatio > 0) {
            int count = counter.incrementAndGet();

            if (count % (1 / sampleRatio) == 0) {
                return offer(event);
            }
            return false;
        } else {
            return offer(event);
        }
    }

    @Override
    public Event peek() {
        return queue.peek();
    }

    @Override
    public Event poll() {
        try {
            return queue.poll(5, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public int size() {
        return queue.size();
    }
}
