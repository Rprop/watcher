package com.jd.jmi.escort.model.enums;

/**
 * Created by changpan on 2016/3/1.
 */
public enum SourceEnum {

    BACKGROUND(1, "后台录入"),
    SYNC(2, "程序同步");

    int code;
    String name;

    SourceEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
