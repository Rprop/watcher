package com.whh.watcher.spi;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public interface BlackListManager {
    /**
     * 应用或者某IP不进行统计
     *
     * @param app 应用名称
     * @param ip  IP地址
     * @return
     */
    public boolean isBlackList(String app, String ip);
}
