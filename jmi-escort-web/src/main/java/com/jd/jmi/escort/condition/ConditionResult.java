package com.jd.jmi.escort.condition;

/**
 * 结果集
 * Created by changpan on 2016/2/18.
 */
public class ConditionResult {

    private boolean success;

    private String mes;

    private String value;

    public ConditionResult(boolean success) {
        this.success = success;
    }

    public ConditionResult(boolean success, String mes, String value) {
        this.success = success;
        this.mes = mes;
        this.value = value == null ? "" : value.trim();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMes() {
        return mes;
    }

    public ConditionResult setMes(String mes) {
        this.mes = mes;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
