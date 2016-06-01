package com.whh.watcher.domain.config;

import com.whh.watcher.util.HostUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class SystemConfig {
    private String appName = "app";//应用的名称
    private String ipAddress = HostUtil.getLocalIp();
    private String hostName = HostUtil.getLocalHostName();
    private String svrAddress = HostUtil.getLocalIp() + ":8888";

    private String tpLogFile = "D:/test1/test.log";
    private String tpLogFileMaxSize = "100M";
    private int tpLogFileMaxNum = 10;

    private int eventBatchSendMaxSize = 20;//最大批量发送数据
    private int eventBufferedMaxSize = 5000;//通道中最大保留的数据量
    private int eventCachedMergeMaxSize = 3000;//缓存要进行合并的事件大小
    private int eventSurvivePeriod = 5000;//消息最大缓存周期

    /**
     * 最大响应时间阀值（毫秒）
     */
    private final static long maxWatchElapsedTime = 1000L;
    private static boolean mergeWatchEvent = true;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getTpLogFile() {
        return tpLogFile;
    }

    public void setTpLogFile(String tpLogFile) {
        this.tpLogFile = tpLogFile;
    }

    public String getTpLogFileMaxSize() {
        return tpLogFileMaxSize;
    }

    public void setTpLogFileMaxSize(String tpLogFileMaxSize) {
        this.tpLogFileMaxSize = tpLogFileMaxSize;
    }

    public int getTpLogFileMaxNum() {
        return tpLogFileMaxNum;
    }

    public void setTpLogFileMaxNum(int tpLogFileMaxNum) {
        this.tpLogFileMaxNum = tpLogFileMaxNum;
    }


    public void setSvrAddress(String svrAddress) {
        this.svrAddress = svrAddress;
    }

    public List<String> getSvrAddress() {
        List<String> list = new ArrayList<String>();
        String[] svrStr = svrAddress.split(",");
        for (int i = 0; i < svrStr.length; i++) {
            if (svrStr[i] != null && svrStr[i].contains(":")) {
                list.add(svrStr[i]);
            }
        }
        return list;
    }

    public boolean isLocalMode() {
        return true;
    }


    public int getEventBatchSendMaxSize() {
        return eventBatchSendMaxSize;
    }

    public int getEventBufferedMaxSize() {
        return eventBufferedMaxSize;
    }

    public int getEventSurvivePeriod() {
        return eventSurvivePeriod;
    }

    public int getEventCachedMergeMaxSize() {
        return eventCachedMergeMaxSize;
    }

    public long getMaxWatchElapsedTime() {
        return maxWatchElapsedTime;
    }

    public boolean canMergeWatchEvent() {
        return mergeWatchEvent;
    }
}
