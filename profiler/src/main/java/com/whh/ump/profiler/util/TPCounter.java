package com.whh.ump.profiler.util;

import com.whh.ump.profiler.CallerInfo;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TP统计用的counter
 *
 * @author chenzehong
 */
public class TPCounter {
    /**
     * 最大响应时间阀值（毫秒）
     */
    public final static long MAX_TP_COUNT_ELAPSED_TIME = 400L;
    /**
     * TP响应时间计数的周期（毫秒）
     */
    private final static long COUNT_TP_PERIOD = 5 * 1000L;
    /**
     * 异步写TP统计数据的延时（毫秒）
     */
    private final static long WRITE_TP_LOG_DELAY = 1000L;
    /**
     * 拼接统计计数KEY的分隔符
     */
    private final static String KEY_STORE_KEY_SPLIT_STR = "###";

    private final static String LINE_SEP = System.getProperty("line.separator");

    private final static TPCounter counter = new TPCounter();
    private ConcurrentHashMap<String, ConcurrentHashMultiSet<Integer>> tpCountMap;
    /**
     * 日志输出模板
     */
    private final static String TP_LOG_TEMPLATE = "{\"time\":" + "\"{}\",\"key\":\"{}\",\"hostname\":" + "\"" + CacheUtil.HOST_NAME
            + "\",\"processState\":" + "\"" + CallerInfo.STATE_TRUE + "\",\"elapsedTime\":\"{}\",\"count\":\"{}\"}";
    /**
     * 自动跑key用
     */
    private final static String AUTO_TP_LOG_TEMPLATE = "{\"time\":" + "\"{}\",\"key\":\"{}\",\"appName\":\"{}\",\"hostname\":" + "\"" + CacheUtil.HOST_NAME
            + "\",\"processState\":" + "\"" + CallerInfo.STATE_TRUE + "\",\"elapsedTime\":\"{}\",\"count\":\"{}\"}";

    private TPCounter() {
        tpCountMap = new ConcurrentHashMap<String, ConcurrentHashMultiSet<Integer>>();

        long lastTimePoint = (new Date().getTime() / COUNT_TP_PERIOD) * COUNT_TP_PERIOD;
        Date firstWriteTime = new Date(lastTimePoint + COUNT_TP_PERIOD);
        Timer writeTPLogTimer = new Timer("UMP-WriteTPLogThread", true);
        writeTPLogTimer.scheduleAtFixedRate(new WriteTPLogTask(), firstWriteTime, COUNT_TP_PERIOD);
    }

    /**
     * 获取counter实例
     *
     * @return
     */
    public static TPCounter getInstance() {
        return counter;
    }

    /**
     * 对TP调用的数据进行统计计数
     *
     * @param callerInfo
     * @param elapsedTime
     */
    public void count(CallerInfo callerInfo, long elapsedTime) {
        String countMapKey = getCountMapKey(callerInfo);

        ConcurrentHashMultiSet<Integer> elapsedTimeCounter = tpCountMap.get(countMapKey);

        if (elapsedTimeCounter == null) {
            // 如果该key对应的统计集合不存在，需要用线程安全的方式往集合里面put统计集合
            ConcurrentHashMultiSet<Integer> newElapsedTimeCounter = new ConcurrentHashMultiSet<Integer>();
            elapsedTimeCounter = tpCountMap.putIfAbsent(countMapKey, newElapsedTimeCounter);
            if (elapsedTimeCounter == null) {
                // put成功，集合里面原来确实没有该统计集合，然后使用新的统计集合
                elapsedTimeCounter = newElapsedTimeCounter;
            }
        }

        // 增加一次该响应时间的计数
        elapsedTimeCounter.add((int) elapsedTime);
    }

    /**
     * 获取统计计数用的key
     *
     * @param callerInfo
     * @return
     */
    private String getCountMapKey(CallerInfo callerInfo) {
        if (null == callerInfo.getAppName() || "".equals(callerInfo.getAppName())) {
            return callerInfo.getKey() + KEY_STORE_KEY_SPLIT_STR + ((System.currentTimeMillis() / COUNT_TP_PERIOD) * COUNT_TP_PERIOD);
        } else {
            return callerInfo.getKey() + KEY_STORE_KEY_SPLIT_STR + callerInfo.getAppName() + KEY_STORE_KEY_SPLIT_STR + ((System.currentTimeMillis() / COUNT_TP_PERIOD) * COUNT_TP_PERIOD);
        }

    }

    /**
     * 往磁盘上写TP统计日志的task
     *
     * @author chenzehong
     */
    private class WriteTPLogTask extends TimerTask {
        @Override
        public void run() {
            try {
                Map<String, ConcurrentHashMultiSet<Integer>> writeCountMap = tpCountMap;
                tpCountMap = new ConcurrentHashMap<String, ConcurrentHashMultiSet<Integer>>();

                writeTPLog(writeCountMap);
            } catch (Throwable ex) {
            }
        }

        /**
         * 输出TP统计数据到tp.log
         *
         * @param writeCountMap
         * @throws InterruptedException
         */
        private void writeTPLog(Map<String, ConcurrentHashMultiSet<Integer>> writeCountMap) throws InterruptedException {
            if (writeCountMap != null) {

                // wait the counter completed
                Thread.sleep(WRITE_TP_LOG_DELAY);
                StringBuilder logs = new StringBuilder(1024);
                for (Map.Entry<String, ConcurrentHashMultiSet<Integer>> entry : writeCountMap.entrySet()) {
                    String[] keyTime = entry.getKey().split(KEY_STORE_KEY_SPLIT_STR);

                    if (keyTime != null && keyTime.length == 2) {
                        String key = keyTime[0].trim();
                        String time = CacheUtil.changeLongToDate(Long.valueOf(keyTime[1].trim()));

                        ConcurrentHashMultiSet<Integer> elapsedTimeCounter = entry.getValue();
                        boolean needSetLineSep = false;

                        for (Integer elapsedTime : elapsedTimeCounter.elementSet()) {
                            if (needSetLineSep) {
                                logs.append(LINE_SEP);
                            } else {
                                needSetLineSep = true;
                            }

                            Integer count = elapsedTimeCounter.count(elapsedTime);
                            String log = LogFormatter.format(TP_LOG_TEMPLATE, time, key, elapsedTime, count);
                            logs.append(log);
                        }

                        int length = logs.length();
                        if (length > 0) {
                            CustomLogger.TpLogger.info(logs.toString());
                            logs.setLength(0);
                        }
                    } else if (keyTime != null && keyTime.length == 3) {
                        String key = keyTime[0].trim();
                        String appName = keyTime[1].trim();
                        String time = CacheUtil.changeLongToDate(Long.valueOf(keyTime[2].trim()));

                        ConcurrentHashMultiSet<Integer> elapsedTimeCounter = entry.getValue();
                        boolean needSetLineSep = false;

                        for (Integer elapsedTime : elapsedTimeCounter.elementSet()) {
                            if (needSetLineSep) {
                                logs.append(LINE_SEP);
                            } else {
                                needSetLineSep = true;
                            }

                            Integer count = elapsedTimeCounter.count(elapsedTime);
                            String log = LogFormatter.format(AUTO_TP_LOG_TEMPLATE, time, key, appName, elapsedTime, count);
                            logs.append(log);
                        }

                        int length = logs.length();
                        if (length > 0) {
                            CustomLogger.TpLogger.info(logs.toString());
                            logs.setLength(0);
                        }
                    }
                }
            }
        }
    }
}