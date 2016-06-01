package com.jd.jmi.escort.model.enums;

/**
 * Created by changpan on 2016/2/25.
 */
public enum SnapTypeEnum {

    EFFECT_RULE(1, "生效规则"),
    USER_MODEL(2, "用户模型"),
    DECIDE_MODEK(3, "判定模型");

    int code;
    String name;

    SnapTypeEnum(int code, String name) {
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
