package com.jd.watcher;

import com.jd.watcher.domain.AlarmEvent;
import com.jd.watcher.domain.CountEvent;
import com.jd.watcher.domain.JvmEvent;
import com.jd.watcher.domain.WatchEvent;
import com.jd.watcher.domain.enums.CountTypeEnum;
import com.jd.watcher.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Dept：业务系统监控门面
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class Watcher {
    private static final Logger logger = LoggerFactory.getLogger(Watcher.class);
    public static Boolean JVM_MONITOR_INIT = false;//JVM初始化时生效

    /**
     * 方法监控点开始方法
     *
     * @param key     应用唯度维一
     * @param bizType 业务类型，将key归类，可按以下维度：controller访问、数据库、缓存、消息服务、service、外部接口
     * @return
     */
    public static WatchEvent watchStart(String key, String bizType) {
        WatchEvent callerInfo = new WatchEvent(key, bizType);
        return callerInfo;
    }

    /**
     * 只有经过end或者异常，本监控数据才会进行发送。
     *
     * @param watchEvent
     */
    public static void watchEnd(WatchEvent watchEvent) {
        if (watchEvent != null) {
            watchEvent.end();
        }
    }

    /**
     * 调用此方法系统将会统计方法的可用率,此方法建议在抛出异常或业务逻辑出现异常时调用
     *
     * @param watchEvent
     */
    public static void watchError(WatchEvent watchEvent, Exception e) {
        try {
            if (watchEvent != null) {
                watchEvent.exception(e);
            }
        } catch (Exception e1) {
            logger.error("统一时异常", e1);
        }
    }

    /**
     * 计算代码块执行的次数
     *
     * @param key 唯一ID
     */
    public static void watchForCount(String key) {
        Watcher.watchForSum(key, 1);
    }

    public static void watchForSum(String key, Number value) {
        CountEvent countEvent = new CountEvent(key, CountTypeEnum.total, Watcher.numberToString(value));
        countEvent.end();
    }

    /**
     * 计算某些数字的均值
     *
     * @param key   唯一ID
     * @param value 要计算的值
     */
    public static void watchForAvg(String key, Number value) {
        CountEvent countEvent = new CountEvent(key, CountTypeEnum.avg, Watcher.numberToString(value));
        countEvent.end();
    }

    /**
     * 多条件多唯度计算结果
     *
     * @param key
     * @param value 要计算的值，如a=15.3&b=6.5&a=12 必须保证格式正确
     */
    public static void watchForMultiAvg(String key, String value) {
        CountEvent countEvent = new CountEvent(key, CountTypeEnum.multiAvg, value.toString());
        countEvent.end();
    }

    private static String numberToString(Number number, String... format) {
        if (number instanceof Double || number instanceof Float) {
            return String.format("%.2f", number);
        } else {
            return number.toString();
        }
    }

    public static void watchAlarm(String key, String alarmInfo) {
        if (alarmInfo != null && alarmInfo.length() > 512) {
            alarmInfo = alarmInfo.substring(0, 512);
        }
        AlarmEvent alarmEvent = new AlarmEvent(key, alarmInfo);
        alarmEvent.end();
    }

    /**
     * 调用此方法将收集jvm启动环境信息，并定时收集jvm运行时信息
     *
     * @param sysKey 系统key值
     * @param jvmKey jvm监控Key值
     */
    public static synchronized void registerJvmData(final String sysKey,final String jvmKey) {
        try {
            if (!JVM_MONITOR_INIT) {
                //定时获取jvm运行时信息
                Timer timer = new Timer("UMP-CollectJvmInfoThread", true);
                //定时推送JVM运行信息
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        JvmEvent.runTimeEvent(sysKey);
                    }
                }, 1000, ConfigFactory.getJvmConfig().getPeriodRunTime());

                //定时推送系统信息
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        JvmEvent.jvmHandle(jvmKey);
                    }

                }, 0, ConfigFactory.getJvmConfig().getPeriodEvn());

            }
        } catch (Throwable t) {
            logger.error("定时收集jvm运行时信息异常：", t);
        }
    }
}
