package com.jd.watcher.config;

import com.jd.watcher.domain.config.JvmConfig;
import com.jd.watcher.domain.config.SystemConfig;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class ConfigFactory {
    private static SystemConfig systemConfig = new SystemConfig();

    /**
     * 获取JVM配置相关信息
     * @return
     */
    public static JvmConfig getJvmConfig(){
        return new JvmConfig();
    }

    public static SystemConfig getSystemConfig(){
        return systemConfig;
    }

}
