package com.whh.watcher.domain.enums;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public enum EventTypeEnum {
    jvm("j", "JVM信息"),
    count("c", "统计计数"),
    alarm("a", "业务报警"),
    watch("w", "普通代码块监控");

    String code;
    String name;

    EventTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
