package com.whh.watcher.receiver;


import com.whh.watcher.domain.Event;
import com.whh.watcher.config.ConfigFactory;
import com.whh.watcher.spi.MessageQueue;
import org.slf4j.Logger;

public abstract class AbstractMessageAnalyzer<R> implements MessageAnalyzer {
    public static final long MINUTE = 60 * 1000L;
    public static final long ONE_HOUR = 60 * 60 * 1000L;
    public static final long ONE_DAY = 24 * ONE_HOUR;

    private long m_extraTime;
    protected long m_startTime;
    protected long m_duration;
    protected Logger m_logger;
    private long m_errors = 0;
    private volatile boolean m_active = true;
    protected int m_index;
    @Override
    public void analyze(MessageQueue queue) {
        while (!isTimeout() && isActive()) {
            Event tree = queue.poll();
            if (tree != null) {
                try {
                    process(tree);
                } catch (Throwable e) {
                    m_errors++;
                    if (m_errors == 1 || m_errors % 10000 == 0) {
                        //todo 统计
                        m_logger.error("需统计出错次数", e);
                    }
                }
            }
        }

        while (true) {
            Event tree = queue.poll();
            if (tree != null) {
                try {
                    process(tree);
                } catch (Throwable e) {
                    m_errors++;

                    if (m_errors == 1 || m_errors % 10000 == 0) {
                        //todo 统计
                        m_logger.error("需统计出错次数", e);
                    }
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void destroy() {

//        ReportManager<?> manager = this.getReportManager();
//        if (manager != null) {
//            manager.destory();
//        }
    }

    @Override
    public abstract void doCheckpoint(boolean atEnd);

    @Override
    public int getAnanlyzerCount() {
        return 1;
    }

    protected long getExtraTime() {
        return m_extraTime;
    }

    public abstract R getReport(String domain);

    @Override
    public long getStartTime() {
        return m_startTime;
    }

    @Override
    public void initialize(long startTime, long duration, long extraTime) {
        m_extraTime = extraTime;
        m_startTime = startTime;
        m_duration = duration;

        loadReports();
    }

    protected boolean isActive() {
        synchronized (this) {
            return m_active;
        }
    }

    protected boolean isLocalMode() {
        return ConfigFactory.getSystemConfig().isLocalMode();
    }

    protected boolean isTimeout() {
        long currentTime = System.currentTimeMillis();
        long endTime = m_startTime + m_duration + m_extraTime;

        return currentTime > endTime;
    }

    protected abstract void loadReports();

    protected abstract void process(Event event);

    public void shutdown() {
        synchronized (this) {
            m_active = false;
        }
    }

    public void setIndex(int index) {
        m_index = index;
    }

}
