package com.jd.ump.profiler.common;

import com.jd.ump.profiler.util.CacheUtil;
import com.jd.ump.profiler.util.CustomLogger;

import java.util.TimerTask;

public class AliveModule extends TimerTask {
    private String key;

    public AliveModule(String key) {
        this.key = key;
    }

    @Override
    public void run() {
        try {
            CustomLogger.AliveLogger.info("{\"key\":" + "\"" + key
                    + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME
                    + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime()
                    + "\"}");
        } catch (Exception e) {
        }
    }
}