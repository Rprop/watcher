package com.whh.watcher.channel;

import com.whh.watcher.domain.Event;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/17
 * Version:1.0
 */
public interface MergeAble {
    /**
     * 判断对象是否可以合并成一个
     *
     * @return
     */
    public boolean canMerge();

    /**
     * 如果可合并，获取相应的唯一值（不同类型的事件，必须保证Key值不同）
     *
     * @return
     */
    public String mergeKey();

    /**
     * 两事件进行合并
     *
     * @param event
     */
    public void merge(Event event);
}
