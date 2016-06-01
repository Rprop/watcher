package com.jd.ump.profiler.common;

import com.jd.ump.profiler.jvm.JvmInfoPickerFactory;
import com.jd.ump.profiler.util.CacheUtil;
import com.jd.ump.profiler.util.CustomLogger;
import com.jd.ump.profiler.util.LogFormatter;

import java.util.Map;
import java.util.Map.Entry;

public class BizModule {
    private final static String LOG_TYPE = "BIZ";

    // 流程监控的类型,分布式跨流程业务监控
    private final static String PROC_TYPE = "4";
    // 流程实例ID
    private final static String INSTANCE_ID = JvmInfoPickerFactory.create(JvmInfoPickerFactory.PICKER_TYPE).getJvmInstanceCode();

    /**
     * 业务监控日志输出模板1
     */
    private final static String BIZ_LOG_TEMPLATE1 = "{\"bTime\":\"{}\""
            + ",\"logtype\":\"" + LOG_TYPE + "\""
            + ",\"bKey\":\"{}\""
            + ",\"bHost\":" + "\"" + CacheUtil.HOST_NAME + "\""
            + ",\"type\":\"{}\""
            + ",\"{}\":\"{}\""
            + "}";
    /**
     * 业务监控日志输出模板2
     */
    private final static String BIZ_LOG_TEMPLATE2 = "{\"bTime\":\"{}\""
            + ",\"logtype\":\"" + LOG_TYPE + "\""
            + ",\"bKey\":\"{}\""
            + ",\"bHost\":" + "\"" + CacheUtil.HOST_NAME + "\""
            + ",\"type\":\"{}\""
            + "{}"
            + "}";

    /**
     * 流程监控日志输出模板
     */
    private final static String PROC_LOG_TEMPLATE = "{\"bTime\":\"{}\""
            + ",\"logtype\":\"" + LOG_TYPE + "\""
            + ",\"bKey\":\"{}\""
            + ",\"bHost\":\"" + CacheUtil.HOST_NAME + "\""
            + ",\"type\":\"" + PROC_TYPE + "\""
            + ",\"iCode\":\"" + INSTANCE_ID + "\""
            + ",\"data\":{}"
            + "}";

    public static void bizHandle(String key, int type, Number value) {
        try {
            CustomLogger.BizLogger.info(LogFormatter.format(BIZ_LOG_TEMPLATE1, CacheUtil.getNowTime(), key, type, (type == 1 ? "bValue" : "bCount"), value));
        } catch (Exception e) {
        }
    }

    public static void bizHandle(String key, int type, Map<String, ?> dataMap) {
        try {
            StringBuilder sb = new StringBuilder(32);

            for (Entry<String, ?> entry : dataMap.entrySet()) {
                sb.append(CacheUtil.COMMA).append(CacheUtil.QUOTATION).append(entry.getKey()).append(CacheUtil.QUOTATION).append(CacheUtil.COLON)
                        .append(CacheUtil.QUOTATION).append(entry.getValue()).append(CacheUtil.QUOTATION);
            }

            Object[] args = new Object[]{CacheUtil.getNowTime(), key, type, sb.toString()};

            CustomLogger.BizLogger.info(LogFormatter.format(BIZ_LOG_TEMPLATE2, args));
        } catch (Exception e) {
        }
    }

    public static void bizNode(String nodeID, String data) {
        CustomLogger.BizLogger.info(LogFormatter.format(PROC_LOG_TEMPLATE, CacheUtil.getNowTime(), nodeID, data));
    }

    public static void bizNode(String nodeID, Map<String, String> data) {
        StringBuilder sb = new StringBuilder("{");

        for (Entry<String, String> entry : data.entrySet()) {
            sb.append(CacheUtil.QUOTATION).append(entry.getKey()).append(CacheUtil.QUOTATION).append(CacheUtil.COLON)
                    .append(CacheUtil.QUOTATION).append(entry.getValue()).append(CacheUtil.QUOTATION).append(CacheUtil.COMMA);
        }

        int length = sb.length();

        if (length > 1) {
            // 去掉结尾的COMMA逗号，
            sb.deleteCharAt(length - 1);
        }

        sb.append("}");

        Object[] args = new Object[]{CacheUtil.getNowTime(), nodeID, sb.toString()};

        CustomLogger.BizLogger.info(LogFormatter.format(PROC_LOG_TEMPLATE, args));
    }
}