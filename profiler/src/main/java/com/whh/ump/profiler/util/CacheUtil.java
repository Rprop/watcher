package com.whh.ump.profiler.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class CacheUtil {
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static final String STR_HOST_ERROR_DETECTED = "** HOST ERROR DETECTED **";
    private static final String STR_IP_ERROR_DETECTED = "** IP ERROR DETECTED **";
    private static final String LOG_TIME_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * 数据定时更新时间间隔 12*3600*1000L
     */
    public static final long UPDATETIME = 12 * 3600 * 1000L;
    /**
     * 系统心跳时间间隔：20秒
     */
    public static final int ALIVETIME = 20000;
    /**
     * JVM监控频率:10秒(运行时信息)
     */
    public static final int JVMTIME_R = 10000;

    /**
     * JVM监控频率:4小时（系统环境信息）
     */
    public static final int JVMTIME_E = 3600000 * 4;

    /**
     * 系统心跳初始化首次调用时生效
     */
    public static Boolean SYSTEM_HEART_INIT = false;

    /**
     * JVM初始化时生效
     */
    public static Boolean JVM_MONITOR_INIT = false;

    /**
     * 双引号
     */
    public static final String QUOTATION = "\"";

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 带扩展子段
     */
    public static final String EXTENSIVE = "1";

    /**
     * 无扩展子段
     */
    public static final String NONEXTENSIVE = "0";

    /**
     * 方法心跳map
     */
    public final static Map<String, Long> FUNC_HB = new HashMap<String, Long>();

    public final static String HOST_NAME = getHostName();
    public final static String HOST_IP = getHostIP();

    private static String getHostName() {
        String host = STR_HOST_ERROR_DETECTED;
        try {
            try {
                // 如果能直接取到正确IP就返回，通常windows下可以
                InetAddress localAddress = InetAddress.getLocalHost();
                host = localAddress.getHostName();
            } catch (Throwable e) {
                InetAddress localAddress = getLocalAddress();
                if (localAddress != null) {
                    host = localAddress.getHostName();
                } else {
                    host = STR_HOST_ERROR_DETECTED;
                }
            }
        } catch (Throwable ex) {
            // ignore
        }

        return host;
    }

    public static String getHostIP() {
        String ip = STR_IP_ERROR_DETECTED;

        try {
            if (getLocalAddress() != null) {
                ip = getLocalAddress().getHostAddress();
            } else {
                ip = STR_IP_ERROR_DETECTED;
            }
        } catch (Throwable ex) {
            // ignore
        }

        return ip;
    }

    /**
     * 获取当前系统时间并转化为 yyyyMMddHHmmssSSS 格式
     *
     * @return
     */
    public static String getNowTime() {
        String nowTime = null;
        try {
            Date rightNow = new Date();
            TimeZone localTimeZone = TimeZone.getTimeZone("GMT+8");
            DateFormat df = new SimpleDateFormat(LOG_TIME_FORMAT);
            df.setTimeZone(localTimeZone);
            nowTime = df.format(rightNow);
        } catch (Exception e) {
            nowTime = "";
        }
        return nowTime;
    }

    /**
     * 将long型时间转化为yyyyMMddHHmmssSSS 格式
     *
     * @param time
     * @return
     */
    public static String changeLongToDate(long time) {
        String nowTime = null;
        try {
            Date date = new Date(time);
            TimeZone localTimeZone = TimeZone.getTimeZone("GMT+8");
            DateFormat df = new SimpleDateFormat(LOG_TIME_FORMAT);
            df.setTimeZone(localTimeZone);
            nowTime = df.format(date);
        } catch (Exception e) {
            nowTime = "";
        }
        return nowTime;
    }

    /**
     * 获取本机有效的外部IP地址，而非内部的回环IP
     *
     * @return
     */
    public static String getLocalIP() {
        InetAddress address = getLocalAddress();
        return address == null ? null : address.getHostAddress();
    }

    private static InetAddress getLocalAddress() {
        InetAddress localAddress = null;
        try {
            // 如果能直接取到正确IP就返回，通常windows下可以
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {

        }

        try {
            // 通过轮询网卡接口来获取IP
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null)
                while (interfaces.hasMoreElements())
                    try {
                        NetworkInterface network = (NetworkInterface) interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = (InetAddress) addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {

                                }
                            }
                        }
                    } catch (Throwable e) {
                    }
        } catch (Throwable e) {
        }
        return localAddress;
    }

    /**
     * 判断是否为有效合法的外部IP，而非内部回环IP
     *
     * @param address
     * @return
     */
    private static boolean isValidAddress(InetAddress address) {
        if ((address == null) || (address.isLoopbackAddress())) {
            return false;
        }

        String ip = address.getHostAddress();

        return (ip != null) && (!"0.0.0.0".equals(ip)) && (!"127.0.0.1".equals(ip)) && (IP_PATTERN.matcher(ip).matches());
    }

}
