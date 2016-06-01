package com.whh.ump.profiler.common;

import com.whh.ump.profiler.jvm.JvmInfoPickerFactory;
import com.whh.ump.profiler.util.CacheUtil;
import com.whh.ump.profiler.util.CustomLogger;
import com.whh.ump.profiler.util.LogFormatter;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 通用的日志输出处理器
 *
 * @author chenzehong
 */
public class CommonModule {
    // 实例ID
    private final static String INSTANCE_ID = JvmInfoPickerFactory.create(JvmInfoPickerFactory.PICKER_TYPE).getJvmInstanceCode();
    private final static String LOG_TEMPLATE = "{\"time\":\"{}\""
            + ",\"host\":\"" + CacheUtil.HOST_NAME + "\""
            + ",\"ip\":\"" + CacheUtil.HOST_IP + "\""
            + ",\"iCode\":\"" + INSTANCE_ID + "\""
            + ",\"type\":\"{}\""
            + ",\"data\":{}"
            + "}";

    /**
     * 通用日志输出接口
     *
     * @param type 日志类型标识
     * @param data 日志数据，标准的json格式
     */
    public static void log(String type, String data) {
        CustomLogger.CommonLogger.info(LogFormatter.format(LOG_TEMPLATE, CacheUtil.getNowTime(), type, data));
    }

    /**
     * 通用日志输出接口
     *
     * @param type 日志类型标识
     * @param data 日志数据
     */
    public static void log(String type, Map<String, String> data) {
        StringBuilder sb = new StringBuilder("{");

        for (Entry<String, String> entry : data.entrySet()) {
            sb.append(CacheUtil.QUOTATION).append(entry.getKey()).append(CacheUtil.QUOTATION).append(CacheUtil.COLON)
                    .append(CacheUtil.QUOTATION).append(entry.getValue()).append(CacheUtil.QUOTATION).append(CacheUtil.COMMA);
        }

        // 去掉结尾的COMMA逗号，
        sb.deleteCharAt(sb.length() - 1).append("}");

        CustomLogger.CommonLogger.info(LogFormatter.format(LOG_TEMPLATE, CacheUtil.getNowTime(), type, sb.toString()));
    }
}