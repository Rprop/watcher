package com.jd.watcher.receiver;

import java.util.List;

public interface MessageAnalyzerManager {
    public List<String> getAnalyzerNames();

    public List<MessageAnalyzer> getAnalyzer(String name, long startTime);

//    /**
//     * 获取最新的分析器列表
//     *
//     * @param name
//     * @return
//     */
//    public List<MessageAnalyzer> getLastAnalyzer(String name);
//
//    /**
//     * 获取当前运行的分析器
//     *
//     * @param name 分析器名称
//     * @return
//     */
//    public List<MessageAnalyzer> getCurrentAnalyzer(String name);


//    /**
//     *
//     * @param name
//     * @return
//     */
//    public List<MessageAnalyzer> getCurrentAnalyzer(String name) {
//        long currentStartTime = getCurrentStartTime();
//        Period period = m_periodManager.findPeriod(currentStartTime);
//
//        if (period != null) {
//            return period.getAnalyzer(name);
//        } else {
//            return null;
//        }
//    }
//
//    private long getCurrentStartTime() {
//        long now = System.currentTimeMillis();
//        long time = now - now % HOUR;
//
//        return time;
//    }

//	public List<MessageAnalyzer> getLastAnalyzer(String name) {
//		long lastStartTime = getCurrentStartTime() - HOUR;
//		Period period = m_periodManager.findPeriod(lastStartTime);
//
//		return period == null ? null : period.getAnalyzer(name);
//	}
}
