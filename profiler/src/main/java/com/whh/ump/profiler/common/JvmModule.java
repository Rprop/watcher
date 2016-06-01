package com.whh.ump.profiler.common;

import com.whh.ump.profiler.jvm.JvmInfoPicker;
import com.whh.ump.profiler.jvm.JvmInfoPickerFactory;
import com.whh.ump.profiler.util.CacheUtil;
import com.whh.ump.profiler.util.CustomLogger;

import java.util.TimerTask;

public class JvmModule extends TimerTask {
    private String key;

    private static JvmInfoPicker localJvm = JvmInfoPickerFactory.create(JvmInfoPickerFactory.PICKER_TYPE);
    private static String instanceCode = localJvm.getJvmInstanceCode();
    private static String logType = "JVM";

    public JvmModule(String key) {
        this.key = key;
    }

    @Override
    public void run() {
        try {
            CustomLogger.JVMLogger.info("{\"logtype\":" + "\"" + logType + "\""
                    + ",\"key\":" + "\"" + key + "\"" + ",\"hostname\":" + "\""
                    + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\""
                    + CacheUtil.getNowTime() + "\"" + ",\"datatype\":" + "\""
                    + "2" + "\"" + ",\"instancecode\":" + "\"" + instanceCode
                    + "\"" + ",\"userdata\":" + localJvm.pickJvmRumtimeInfo() + "}");
        } catch (Throwable e) {
        }
    }

    public static void jvmHandle(String jvmKey) {
        try {
            CustomLogger.JVMLogger.info("{\"logtype\":" + "\"" + logType + "\""
                    + ",\"key\":" + "\"" + jvmKey + "\"" + ",\"hostname\":" + "\""
                    + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\""
                    + CacheUtil.getNowTime() + "\"" + ",\"datatype\":" + "\""
                    + "1" + "\"" + ",\"instancecode\":" + "\"" + instanceCode
                    + "\"" + ",\"userdata\":" + localJvm.pickJvmEnvironmentInfo() + "}");
        } catch (Throwable e) {
        }
    }
}