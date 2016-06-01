package com.jd.watcher.spi.imp;

import com.jd.watcher.spi.BlackListManager;
import org.springframework.stereotype.Service;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
@Service("blackListManager")
public class BlackListManagerImp implements BlackListManager {
    @Override
    public boolean isBlackList(String app, String ip) {
        return false;
    }
}
