package com.jd.watcher.receiver;

import java.util.List;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/11
 * Version:1.0
 */
public interface PeriodManager {
    /**
     * 查找一个事件所在的周期
     *
     * @param timestamp
     * @return
     */
    public List<Period> findPeriod(long timestamp);

    /**
     * 开启某个时间的所有周期性统计工作
     *
     * @param startTime
     */
    public void startPeriod(long startTime);

}
