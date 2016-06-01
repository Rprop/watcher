package com.whh.watcher.receiver;

public class PeriodStrategy {
    private long m_duration;//一个周期持续时间
    private long m_extraTime;
    private long m_aheadTime;
    private long m_lastStartTime;
    private long m_lastEndTime;

    public PeriodStrategy(long duration, long extraTime, long aheadTime) {
        m_duration = duration;
        m_extraTime = extraTime;
        m_aheadTime = aheadTime;
        m_lastStartTime = -1;
        m_lastEndTime = 0;
    }

    public long getDuration() {
        return m_duration;
    }

    public long next(long now) {
        long startTime = now - now % m_duration;
        // for current period
        if (startTime > m_lastStartTime) { //同一时间周期之内
            m_lastStartTime = startTime;
            return startTime;
        }

        // prepare next period ahead
        if (now - m_lastStartTime >= m_duration - m_aheadTime) {//下一时间段数据开始统计
            m_lastStartTime = startTime + m_duration;
            return startTime + m_duration;
        }

        // last period is over
        if (now - m_lastEndTime >= m_duration + m_extraTime) {//本次统计已结束
            long lastEndTime = m_lastEndTime;
            m_lastEndTime = startTime;
            return -lastEndTime;
        }

        return 0;
    }
}