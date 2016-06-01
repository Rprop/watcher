package com.jd.watcher.receiver.impl;

import com.jd.watcher.receiver.MessageAnalyzerManager;
import com.jd.watcher.receiver.Period;
import com.jd.watcher.receiver.PeriodStrategy;
import com.jd.watcher.statistic.ServerStatisticManager;
import com.jd.watcher.utils.Threads;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PeriodManager implements Threads.Task {
    private PeriodStrategy m_strategy;

    private List<Period> periods = new ArrayList<Period>();

    private boolean m_active;

    private MessageAnalyzerManager m_analyzerManager;
    private ServerStatisticManager m_serverStateManager;

    private Logger logger;

    public static long ExtraTime = 3 * 60 * 1000L;

    public PeriodManager(long duration, MessageAnalyzerManager analyzerManager,
                         ServerStatisticManager serverStateManager) {
        m_strategy = new PeriodStrategy(duration, ExtraTime, ExtraTime);
        m_active = true;
        m_analyzerManager = analyzerManager;
        m_serverStateManager = serverStateManager;
    }

    private void endPeriod(long startTime) {
        int len = periods.size();
        for (int i = 0; i < len; i++) {
            Period period = periods.get(i);
            if (period.isIn(startTime)) {
                period.finish();
                periods.remove(i);
                break;
            }
        }
    }

    public Period findPeriod(long timestamp) {
        for (Period period : periods) {
            if (period.isIn(timestamp)) {
                return period;
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "RealTimeConsumer-PeriodManager";
    }

    public void init() {
        long startTime = m_strategy.next(System.currentTimeMillis());
        startPeriod(startTime);
    }

    @Override
    public void run() {
        while (m_active) {
            try {
                long value = m_strategy.next(System.currentTimeMillis());
                if (value > 0) {
                    startPeriod(value);
                } else if (value < 0) {
                    // last period is over,make it asynchronous
                    Threads.forGroup("cat").start(new EndTaskThread(-value));
                }
            } catch (Throwable e) {
                logger.error("定时任务运行异常：", e);
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void shutdown() {
        m_active = false;
    }

    private void startPeriod(long startTime) {
        long endTime = startTime + m_strategy.getDuration();
        Period period = new Period(startTime, endTime, m_analyzerManager, m_serverStateManager);
        periods.add(period);
        period.start();
    }

    private class EndTaskThread implements Threads.Task {

        private long m_startTime;

        public EndTaskThread(long startTime) {
            m_startTime = startTime;
        }

        @Override
        public String getName() {
            return "End-Consumer-Task";
        }

        @Override
        public void run() {
            endPeriod(m_startTime);
        }

        @Override
        public void shutdown() {
        }
    }
}