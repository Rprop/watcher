package com.whh.ump.profiler.common;

import com.whh.ump.profiler.util.CacheUtil;
import com.whh.ump.profiler.util.CustomLogger;

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