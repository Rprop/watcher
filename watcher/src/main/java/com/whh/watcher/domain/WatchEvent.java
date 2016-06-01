package com.whh.watcher.domain;

import com.alibaba.fastjson.JSON;
import com.whh.watcher.channel.MergeAble;
import com.whh.watcher.config.ConfigFactory;
import com.whh.watcher.domain.enums.EventTypeEnum;
import com.whh.watcher.channel.EventChannel;
import com.whh.watcher.util.MilliSecondTimer;
import com.whh.watcher.util.ParentIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Dept：代码块执行时间统计
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class WatchEvent extends Event implements MergeAble {
    private static Logger logger = LoggerFactory.getLogger(WatchEvent.class);
    private String parentId;//上次消息的ID,为形成树状消息的形式
    private String bizType;
    private boolean ok;
    private Integer count = 1;
    private Long elapsedTime;
    private String error;

    private WatchEvent() {
    }

    /**
     * 事件初始化，并构建事件树
     * 1、配置相应的本身信息
     * 2、设置上级事件ID
     * 3、将本事件ID放入线程缓存。
     *
     * @param key
     * @param bizType
     */
    public WatchEvent(String key, String bizType) {
        this.event = EventTypeEnum.watch;
        this.key = key;
        this.bizType = bizType;
        this.ok = true;
        this.parentId = ParentIdUtil.getParentId();
        ParentIdUtil.setParentId(this.getId());
    }


    /**
     * 系统异常时，将消息发送，并记录异常信息
     *
     * @param e
     */
    public void exception(Exception e) {
        try {
            this.ok = false;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            this.error = sw.toString();
            this.end();
        } catch (Exception e1) {
            logger.error("发送消息异常！", e1);
        }
    }

    /**
     * 代码块监控完成
     * 1、将消息发送
     * 2、清空线程中的缓存
     */
    @Override
    public void end() {
        try {
            this.elapsedTime = MilliSecondTimer.currentTimeMillis() - time;
            EventChannel.collectEvent(this);
        } catch (Exception e) {
            logger.error("发送消息异常！", e);
        } finally {
            ParentIdUtil.remove();
        }
    }

    @Override
    public String toJsonString() {
        return "W" + JSON.toJSONString(this);
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getBizType() {
        return bizType;
    }

    public boolean isOk() {
        return ok;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean canMerge() {
        if (!ok) {
            return false;
        } else if (this.elapsedTime > ConfigFactory.getSystemConfig().getMaxWatchElapsedTime()) {
            return false;
        } else if (!ConfigFactory.getSystemConfig().canMergeWatchEvent()) {
            return false;
        }
        return true;
    }

    @Override
    public String mergeKey() {
        return new StringBuffer().append(getKey()).append("_").append(elapsedTime).toString();
    }

    @Override
    public void merge(Event event) {
        this.count++;
    }
}