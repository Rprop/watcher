package com.jd.jmi.escort.model;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险事件查询对象
 * Created by xuyonggang on 2016/1/4.
 */
public class RiskEventModel extends PageBaseQuery implements Serializable {

    private static final long serialVersionUID = -5209619745347249298L;

    /**
     * 订单金额 起 分为单位
     */
    public Long totalFeeSta;

    /**
     * 订单金额 止 分为单位
     */
    public Long totalFeeEnd;

    /**
     * 订单金额 起 元为单位
     */
    public String totalFeeStaYuan;

    /**
     * 订单金额 止 元为单位
     */
    public String totalFeeEndYuan;

    /**
     * 用户pin
     */
    @Length(max = 50)
    public String userPin = "";

    /**
     * erpOrderId
     */
    public Long erpOrderId;

    /**
     * 订单类型
     */
    public Integer orderType;

    /**
     * 风控生效规则id
     */
    public Long effectRuleId;

    /**
     * 触发时间 起
     */
    public String strEventTimeSta = "";

    /**
     * 触发时间 止
     */
    public String strEventTimeEnd = "";


    /**
     * 触发时间 起
     */
    public Date eventTimeSta;

    /**
     * 触发时间 止
     */
    public Date eventTimeEnd;

    private Integer isDelUser; //是否是删除黑名单事件   1：是  0： 否

    private Integer level; // 事件级别

    public Long getTotalFeeSta() {
        return totalFeeSta;
    }

    public void setTotalFeeSta(Long totalFeeSta) {
        this.totalFeeSta = totalFeeSta;
    }

    public Long getTotalFeeEnd() {
        return totalFeeEnd;
    }

    public void setTotalFeeEnd(Long totalFeeEnd) {
        this.totalFeeEnd = totalFeeEnd;
    }

    public String getUserPin() {
        return userPin.trim();
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public Long getErpOrderId() {
        return erpOrderId;
    }

    public void setErpOrderId(Long erpOrderId) {
        this.erpOrderId = erpOrderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getEffectRuleId() {
        return effectRuleId;
    }

    public void setEffectRuleId(Long effectRuleId) {
        this.effectRuleId = effectRuleId;
    }

    public Date getEventTimeSta() {
        return eventTimeSta;
    }

    public void setEventTimeSta(Date eventTimeSta) {
        this.eventTimeSta = eventTimeSta;
    }

    public Date getEventTimeEnd() {
        return eventTimeEnd;
    }

    public void setEventTimeEnd(Date eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd;
    }

    public String getStrEventTimeSta() {
        return strEventTimeSta;
    }

    public void setStrEventTimeSta(String strEventTimeSta) {
        this.strEventTimeSta = strEventTimeSta;
    }

    public String getStrEventTimeEnd() {
        return strEventTimeEnd;
    }

    public void setStrEventTimeEnd(String strEventTimeEnd) {
        this.strEventTimeEnd = strEventTimeEnd;
    }

    public String getTotalFeeStaYuan() {
        return totalFeeStaYuan;
    }

    public void setTotalFeeStaYuan(String totalFeeStaYuan) {
        this.totalFeeStaYuan = totalFeeStaYuan;
    }

    public String getTotalFeeEndYuan() {
        return totalFeeEndYuan;
    }

    public void setTotalFeeEndYuan(String totalFeeEndYuan) {
        this.totalFeeEndYuan = totalFeeEndYuan;
    }

    public Integer getIsDelUser() {
        return isDelUser;
    }

    public void setIsDelUser(Integer isDelUser) {
        this.isDelUser = isDelUser;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
