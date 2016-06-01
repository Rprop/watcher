package com.whh.ump.profiler;

import com.whh.ump.profiler.util.CacheUtil;
import com.whh.ump.profiler.util.CustomLogger;
import com.whh.ump.profiler.util.LogFormatter;

public class CallerInfo {
    public final static int STATE_TRUE = 0;
    public final static int STATE_FALSE = 1;

    private final static String TP_LOG_TEMPLATE = "{\"time\":" + "\"{}\",\"key\":\"{}\",\"hostname\":\"" + CacheUtil.HOST_NAME
            + "\",\"processState\":" + "\"{}\",\"elapsedTime\":\"{}\"}";
    /**
     * 自动跑key用，增加了应用名称
     */
    private final static String AUTO_TP_LOG_TEMPLATE = "{\"time\":" + "\"{}\",\"key\":\"{}\",\"appName\":\"{}\",\"hostname\":\"" + CacheUtil.HOST_NAME
            + "\",\"processState\":" + "\"{}\",\"elapsedTime\":\"{}\"}";

    private long startTime;

    private String key;
    private String appName;
    private int processState;
    private long elapsedTime;

    public CallerInfo(String key) {
        this.key = key;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = -1L;
        this.processState = STATE_TRUE;
    }

    public CallerInfo(String key, String appName) {
        this.key = key;
        this.appName = appName;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = -1L;
        this.processState = STATE_TRUE;
    }

    public int getProcessState() {
        return processState;
    }

    /**
     * 此方法是设置SLA信息块中的方法运行状态,此方法建议在抛出异常或业务逻辑出现异常时调用，
     */
    public void error() {
        processState = STATE_FALSE;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getKey() {
        return key;
    }

    public String getAppName() {
        return appName;
    }

    public long getElapsedTime() {
        if (elapsedTime == -1L) {
            elapsedTime = System.currentTimeMillis() - startTime;
        }
        return elapsedTime;
    }

    public void stop() {
        if (null == appName || "".equals(appName)) {
            CustomLogger.TpLogger.info(this.toString());
        } else {
            CustomLogger.TpLogger.info(packagLogInfo());
        }
    }

    @Override
    public String toString() {
        return LogFormatter.format(TP_LOG_TEMPLATE, CacheUtil.getNowTime(), key, processState, getElapsedTime());
    }

    private String packagLogInfo() {
        return LogFormatter.format(AUTO_TP_LOG_TEMPLATE, CacheUtil.getNowTime(), key, appName, processState, getElapsedTime());
    }
}