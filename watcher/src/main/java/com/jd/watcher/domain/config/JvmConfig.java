package com.jd.watcher.domain.config;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class JvmConfig {
    private static final int jvmTimeRun = 10000;//JVM监控频率:10秒(运行时信息)
    private static final int jvmTimeEvn = 3600000 * 4;//JVM监控频率:4小时（系统环境信息）
    private int periodRunTime = jvmTimeRun;//运行信息上报周期
    private int periodEvn = jvmTimeEvn;//运行环境上报周期
    private boolean pauseSend;//数据是否暂停同步

    public int getPeriodRunTime() {
        return periodRunTime;
    }

    public void setPeriodRunTime(int periodRunTime) {
        this.periodRunTime = periodRunTime;
    }

    public int getPeriodEvn() {
        return periodEvn;
    }

    public void setPeriodEvn(int periodEvn) {
        this.periodEvn = periodEvn;
    }

    public boolean isPauseSend() {
        return pauseSend;
    }

    public void setPauseSend(boolean pauseSend) {
        this.pauseSend = pauseSend;
    }
}
