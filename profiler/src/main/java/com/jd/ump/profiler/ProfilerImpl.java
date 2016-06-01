package com.jd.ump.profiler;

import com.jd.ump.profiler.common.*;
import com.jd.ump.profiler.util.CacheUtil;
import com.jd.ump.profiler.util.CustomLogger;
import com.jd.ump.profiler.util.TPCounter;

import java.util.*;

public final class ProfilerImpl {
    private final static TPCounter tpCounter = TPCounter.getInstance();
    public static List<Long> profiler = new ArrayList<Long>();
    /**
     * 方法监控点开始方法
     *
     * @param key         在网站上注册的方法监控点的key
     * @param enableHeart true代表开启方法心跳监控，false则关闭
     * @param enableTP    true代表开启方法性能监控，false则关闭
     * @return
     */
    public static CallerInfo scopeStart(String key, boolean enableHeart, boolean enableTP) {
        CallerInfo callerInfo = null;

        try {
            if (enableTP) {
                callerInfo = new CallerInfo(key);
            }

            if (enableHeart) {
                if (!CacheUtil.FUNC_HB.containsKey(key)) {
                    // 注册心跳点
                    synchronized (CacheUtil.FUNC_HB) {
                        if (!CacheUtil.FUNC_HB.containsKey(key)) {
                            // 添加方法心跳信息
                            CacheUtil.FUNC_HB.put(key,
                                    System.currentTimeMillis());
                            CustomLogger.AliveLogger.info("{\"key\":" + "\""
                                    + key + "\"" + ",\"hostname\":"
                                    + "\"" + CacheUtil.HOST_NAME
                                    + "\"" + ",\"time\":" + "\""
                                    + CacheUtil.getNowTime() + "\"}");
                        }
                    }
                } else {
                    Long hbPoint = CacheUtil.FUNC_HB.get(key);
                    if ((System.currentTimeMillis() - hbPoint) >= 20000L) {
                        // 每20秒发送一次方法心跳
                        synchronized (CacheUtil.FUNC_HB) {
                            if ((System.currentTimeMillis() - CacheUtil.FUNC_HB.get(key)) >= 20000L) {
                                CacheUtil.FUNC_HB.put(key, System.currentTimeMillis());
                                CustomLogger.AliveLogger.info("{\"key\":" + "\""
                                        + key + "\"" + ",\"hostname\":"
                                        + "\"" + CacheUtil.HOST_NAME
                                        + "\"" + ",\"time\":" + "\""
                                        + CacheUtil.getNowTime() + "\"}");
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
        }
        return callerInfo;
    }

    /**
     * 支持自动跑key用，增加appName参数
     *
     * @param key
     * @param appName
     * @param enableHeart
     * @param enableTP
     * @return
     */
    public static CallerInfo scopeStart(String key, String appName, boolean enableHeart, boolean enableTP) {
        CallerInfo callerInfo = null;

        try {
            if (enableTP) {
                callerInfo = new CallerInfo(key, appName);
            }

            if (enableHeart) {
                if (!CacheUtil.FUNC_HB.containsKey(key)) {
                    // 注册心跳点
                    synchronized (CacheUtil.FUNC_HB) {
                        if (!CacheUtil.FUNC_HB.containsKey(key)) {
                            // 添加方法心跳信息
                            CacheUtil.FUNC_HB.put(key,
                                    System.currentTimeMillis());
                            CustomLogger.AliveLogger.info("{\"key\":" + "\""
                                    + key + "\"" + ",\"hostname\":"
                                    + "\"" + CacheUtil.HOST_NAME
                                    + "\"" + ",\"time\":" + "\""
                                    + CacheUtil.getNowTime() + "\"}");
                        }
                    }
                } else {
                    Long hbPoint = CacheUtil.FUNC_HB.get(key);
                    if ((System.currentTimeMillis() - hbPoint) >= 20000L) {
                        // 每20秒发送一次方法心跳
                        synchronized (CacheUtil.FUNC_HB) {
                            if ((System.currentTimeMillis() - CacheUtil.FUNC_HB.get(key)) >= 20000L) {
                                CacheUtil.FUNC_HB.put(key, System.currentTimeMillis());
                                CustomLogger.AliveLogger.info("{\"key\":" + "\""
                                        + key + "\"" + ",\"hostname\":"
                                        + "\"" + CacheUtil.HOST_NAME
                                        + "\"" + ",\"time\":" + "\""
                                        + CacheUtil.getNowTime() + "\"}");
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
        }
        return callerInfo;
    }

    public static void scopeEnd(CallerInfo callerInfo) {
        try {
            if (callerInfo != null) {
                // 超过最大消耗时间的TP数据和调用失败的统计就不用走计数方式
                if (callerInfo.getProcessState() == CallerInfo.STATE_FALSE) {
                    callerInfo.stop();
                } else {
                    long elapsedTime = callerInfo.getElapsedTime();
                    if (elapsedTime >= TPCounter.MAX_TP_COUNT_ELAPSED_TIME) {
                        callerInfo.stop();
                    } else {
                        tpCounter.count(callerInfo, elapsedTime);
                    }
                }
            }
        } catch (Throwable t) {
        }
    }

    public static synchronized void scopeAlive(String key) {
        try {
            if (!CacheUtil.SYSTEM_HEART_INIT) {
                Timer timer = new Timer("UMP-AliveThread", true);
                timer.scheduleAtFixedRate(new AliveModule(key), 1000, CacheUtil.ALIVETIME);
                CacheUtil.SYSTEM_HEART_INIT = true;
            }
        } catch (Throwable t) {
        }
    }

    /**
     * @param key
     * @param type
     * @param value
     * @param detail
     */
    public static void registerBusiness(String key, int type,
                                        int value, String detail) {
        try {
            BusinessModule.businessHandle(key, type, value, detail);
        } catch (Throwable t) {
        }
    }

    /**
     * @param key
     * @param type
     * @param value
     * @param detail
     * @param RTXInfo
     * @param MailInfo
     * @param SMSInfo
     */
    public static void registerBusiness(String key, int type,
                                        int value, String detail, String rtxList, String mailList,
                                        String smsList) {
        try {
            BusinessModule.businessHandle(key, type, value, detail, rtxList, mailList, smsList);
        } catch (Throwable t) {
        }
    }

    /**
     * 调用此方法系统将会统计方法的可用率,此方法建议在抛出异常或业务逻辑出现异常时调用
     *
     * @param callerInfo
     */
    public static void scopeFunctionError(CallerInfo callerInfo) {
        try {
            if (callerInfo != null) {
                callerInfo.error();
            }
        } catch (Throwable t) {
        }
    }

    /**
     * 调用此方法将产生一条累加日志
     *
     * @param key
     * @param type
     * @param value
     */
    public static void registerBizData(String key, int type, Number value) {
        try {
            BizModule.bizHandle(key, type, value);
        } catch (Throwable t) {
        }
    }

    /**
     * 调用此方法将记录一条业务原始数据
     *
     * @param key
     * @param type
     * @param dataMap
     */
    public static void registerBizData(String key, int type, Map<String, ?> dataMap) {
        try {
            BizModule.bizHandle(key, type, dataMap);
        } catch (Throwable t) {
        }
    }

    /**
     * 调用此方法将收集jvm启动环境信息，并定时收集jvm运行时信息
     *
     * @param key
     */
    public static synchronized void registerJvmData(final String key) {
        try {
            if (!CacheUtil.JVM_MONITOR_INIT) {

                //定时获取jvm运行时信息
                Timer timer = new Timer("UMP-CollectJvmInfoThread", true);
                timer.scheduleAtFixedRate(new JvmModule(key), 1000, CacheUtil.JVMTIME_R);

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        JvmModule.jvmHandle(key);
                    }

                }, 0, CacheUtil.JVMTIME_E);

                CacheUtil.JVM_MONITOR_INIT = true;
            }
        } catch (Throwable t) {
        }
    }

    /**
     * 调用此方法将记录一条流程数据
     *
     * @param nodeId 流程节点标志
     * @param data   用户自定义数据项
     */
    public static void bizNode(String nodeID, Map<String, String> data) {
        BizModule.bizNode(nodeID, data);
    }

    /**
     * 调用此方法将记录一条流程数据
     *
     * @param nodeID 流程节点标志
     * @param data   用户自定义数据项， 标准的json格式
     */
    public static void bizNode(String nodeID, String data) {
        BizModule.bizNode(nodeID, data);
    }

    /**
     * 通用日志输出接口
     *
     * @param type 日志类型标识
     * @param data 日志数据，标准的json格式
     */
    public static void log(String type, String data) {
        CommonModule.log(type, data);
    }

    /**
     * 通用日志输出接口
     *
     * @param type 日志类型标识
     * @param data 日志数据
     */
    public static void log(String type, Map<String, String> data) {
        CommonModule.log(type, data);
    }
}