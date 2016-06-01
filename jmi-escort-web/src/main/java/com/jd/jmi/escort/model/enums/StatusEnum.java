package com.jd.jmi.escort.model.enums;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/5
 * Version:1.0
 */
public enum StatusEnum {
    USABLE(1, "可用"),
    UNUSABLE(0, "不可用"),
    DEL(-1, "已删除");
    int code;
    String name;
    StatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
