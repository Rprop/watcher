package com.jd.jmi.escort.model;

import java.io.Serializable;

/**
 * Created by changpan on 2016/1/4.
 * 限制模型
 */
public class ConditionModel implements Serializable{


    private static final long serialVersionUID = -1600687041004312992L;

    /**
     * 限制名称
     */
    private String conditionName;

    /**
     * 符号
     */
    private String operator;

    /**
     * 期望值
     */
    private String expectValue;

    public ConditionModel() {
    }

    public ConditionModel(String conditionName, String operator, String expectValue) {
        this.conditionName = conditionName;
        this.operator = operator;
        this.expectValue = expectValue;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }
}
