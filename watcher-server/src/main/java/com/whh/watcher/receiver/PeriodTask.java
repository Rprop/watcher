package com.whh.watcher.receiver;

import com.whh.watcher.CatConstants;
import com.whh.watcher.domain.Event;
import com.whh.watcher.spi.MessageQueue;
import com.whh.watcher.utils.Threads;
import org.slf4j.Logger;

import java.util.Calendar;

public class PeriodTask implements Threads.Task {
    private MessageAnalyzer m_analyzer;
    private MessageQueue m_queue;
    private long m_startTime;
    private int m_queueOverflow;
    private Logger m_logger;

    private int m_index;

    public void setIndex(int index) {
        m_index = index;
    }

    public PeriodTask(MessageAnalyzer analyzer, MessageQueue queue, long startTime) {
        m_analyzer = analyzer;
        m_queue = queue;
        m_startTime = startTime;
    }

    /**
     * 将消息放入队列
     * @param tree
     * @return
     */
    public boolean enqueue(Event tree) {
        boolean result = m_queue.offer(tree);

        if (!result) { // trace queue overflow
            m_queueOverflow++;

            if (m_queueOverflow % (10 * CatConstants.ERROR_COUNT) == 0) {
                m_logger.warn(m_analyzer.getClass().getSimpleName() + " queue overflow number " + m_queueOverflow);
            }
        }
        return result;
    }

    public void finish() {
        try {
            m_analyzer.doCheckpoint(true);
            m_analyzer.destroy();
        } catch (Exception e) {
            m_logger.error("任务结束异常：", e);
        }
    }

    public MessageAnalyzer getAnalyzer() {
        return m_analyzer;
    }

    @Override
    public String getName() {
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(m_startTime);
        return m_analyzer.getClass().getSimpleName() + "-" + cal.get(Calendar.HOUR_OF_DAY) + "-" + m_index;
    }

    @Override
    public void run() {
        try {
            m_analyzer.analyze(m_queue);
        } catch (Exception e) {
            m_logger.error("分析任务失败：", e);
        }
    }

    @Override
    public void shutdown() {
        //todo 未完成
//		if (m_analyzer instanceof AbstractMessageAnalyzer) {
//			((AbstractMessageAnalyzer<?>) m_analyzer).shutdown();
//		}
    }
}