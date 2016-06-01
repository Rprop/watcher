package com.jd.watcher.util;


import com.jd.watcher.config.ConfigFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageIdUtils {
    private volatile static AtomicInteger index = new AtomicInteger(0);
    private static String domain = ConfigFactory.getSystemConfig().getAppName();
    private static String ipHexStr = HostUtil.ipToHexStr(ConfigFactory.getSystemConfig().getIpAddress());
    private static long timestamp = System.currentTimeMillis() / 1000;

    /**
     * 获取集群下消息的唯一ID
     *
     * @return
     */
    public static String getNextMessId() {
        Long nowTime = MilliSecondTimer.currentTimeMillis() / 1000;
        synchronized (MessageIdUtils.class) {
            if (!nowTime.equals(timestamp)) {
                timestamp = nowTime;
                index = new AtomicInteger(0);
            }
        }
        StringBuilder messId = new StringBuilder(domain.length() + 32);
        messId.append(domain);
        messId.append('-');
        messId.append(ipHexStr);
        messId.append('-');
        messId.append(System.currentTimeMillis() / 1000);
        messId.append('-');
        messId.append(index.incrementAndGet());
        return messId.toString();
    }


    public static void main(String[] args) {
        Long time = MilliSecondTimer.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            getNextMessId();
        }
        System.out.println(MilliSecondTimer.currentTimeMillis() - time);
    }
}
