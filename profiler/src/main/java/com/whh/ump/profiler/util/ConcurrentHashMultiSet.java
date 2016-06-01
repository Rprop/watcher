package com.whh.ump.profiler.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发的hash多set集合
 *
 * @param <E>
 * @author chenzehong
 */
public class ConcurrentHashMultiSet<E> {
    // 统计用的map
    private final transient ConcurrentMap<E, AtomicInteger> counterMap;

    public ConcurrentHashMultiSet() {
        counterMap = new ConcurrentHashMap<E, AtomicInteger>();
    }

    /**
     * 往集合里面增加元素
     *
     * @param element 要增加的元素
     * @return
     */
    public int add(E element) {
        if (element == null) {
            return 0;
        }

        AtomicInteger existingCounter = counterMap.get(element);
        if (existingCounter == null) {
            // 如果该元素不存在，需要用线程安全的方式往集合里面put计数器
            AtomicInteger newCounter = new AtomicInteger();
            existingCounter = counterMap.putIfAbsent(element, newCounter);
            if (existingCounter == null) {
                // put成功，集合里面原来确实没有该元素，然后使用新元素的计数器
                existingCounter = newCounter;
            }
        }

        // 返回原子增量计算的count值
        return existingCounter.incrementAndGet();
    }

    /**
     * 获取所有的元素对象集合
     *
     * @return
     */
    public Set<E> elementSet() {
        return counterMap.keySet();
    }

    /**
     * 获取指定元素的计算值
     *
     * @param element
     * @return
     */
    public int count(E element) {
        if (element == null) {
            return 0;
        } else {
            AtomicInteger existingCounter = counterMap.get(element);
            return (existingCounter == null) ? 0 : existingCounter.get();
        }
    }
}