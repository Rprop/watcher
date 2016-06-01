package com.jd.jmi.escort.pojo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class RiskEvent implements Serializable {

    private static final long serialVersionUID = -4820680653440164575L;

    /** 自增主键 */
    private Long id;

    /** erp订单号 */
    private Long erpOrderId;

    /** 订单类型 */
    @NotNull(message = "订单类型不能为空")
    private Integer orderType;

    /** 用户Pin */
    @NotBlank(message = "用户Pin不能为空")
    @Length(max = 50, message = "用户Pin长度不能超过50")
    private String userPin;

    /** 风控类型-风控生效规则名 */
    @NotBlank(message = "风控生效规则名不能为空")
    @Length(max = 200, message = "风控生效规则名长度不能超过200")
    private String effectRuleName;

    /** 风控生效规则id */
    @NotNull(message = "风控生效规则id不能为空")
    private Long effectRuleId;

    /** 生效规则描述id */
    private String effectDesId;

    /** 风险用户模型id */
    @NotNull(message = "风险用户模型id")
    @DecimalMin(value = "0", message = "风险用户模型最小值为0")
    private Long riskUserModelId;

    /** 判定模型id */
    @NotNull(message = "判定模型id不能为空")
    @DecimalMin(value = "0", message = "判定模型id最小值为0")
    private Long decideModelId;

    /** 订单金额 分为单位 */
    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0", message = "订单金额最小值为0")
    private Long totalFee;

    /** 下单ip */
    @NotBlank(message = "下单ip不能为空")
    @Length(max = 50, message = "下单ip长度不能超过50")
    private String userIp;

    /** 风险程度指数 */
    @NotNull(message = "风险程度指数不能为空")
    @DecimalMax(value = "10", message = "风险程度指数最大值为10")
    @DecimalMin(value = "-10", message = "风险程度指数最小值为-10")
    private int level;

    /** 触发时间 */
    @NotNull(message = "触发时间不能为空")
    private Date triggerDate;

    /** 创建时间 */
    private Date created;

    private String totalFeeYuan;

    @NotNull(message = "规则快照ID不能为空")
    private Long ruleSnapId;//规则快照

    @NotNull(message = "风险对象快照ID不能为空")
    private Long objectSnapId;//风险对象快照

    @NotNull(message = "风险判定快照ID不能为空")
    private Long decideSnapId;//风险判定快照

    private Integer source;//风险来源

    private String sourceName;//风险来源name

    private Integer isDelUser; //是否是删除黑名单事件   1：是  0： 否

    private Integer blackUserLevel; //黑名单用户级别  1：黑名单  2：风险用户

    @NotBlank(message = "唯一标识不能为空")
    @Length(max = 36, message = "唯一标识长度不能超过36")
    private String uuid; // 唯一标识

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public String getEffectRuleName() {
        return effectRuleName;
    }

    public void setEffectRuleName(String effectRuleName) {
        this.effectRuleName = effectRuleName;
    }

    public Long getEffectRuleId() {
        return effectRuleId;
    }

    public void setEffectRuleId(Long effectRuleId) {
        this.effectRuleId = effectRuleId;
    }

    public String getEffectDesId() {
        return effectDesId;
    }

    public void setEffectDesId(String effectDesId) {
        this.effectDesId = effectDesId;
    }

    public Long getRiskUserModelId() {
        return riskUserModelId;
    }

    public void setRiskUserModelId(Long riskUserModelId) {
        this.riskUserModelId = riskUserModelId;
    }

    public Long getDecideModelId() {
        return decideModelId;
    }

    public void setDecideModelId(Long decideModelId) {
        this.decideModelId = decideModelId;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

    public String getTotalFeeYuan() {
        return totalFeeYuan;
    }

    public void setTotalFeeYuan(String totalFeeYuan) {
        this.totalFeeYuan = totalFeeYuan;
    }

    public Long getRuleSnapId() {
        return ruleSnapId;
    }

    public void setRuleSnapId(Long ruleSnapId) {
        this.ruleSnapId = ruleSnapId;
    }

    public Long getObjectSnapId() {
        return objectSnapId;
    }

    public void setObjectSnapId(Long objectSnapId) {
        this.objectSnapId = objectSnapId;
    }

    public Long getDecideSnapId() {
        return decideSnapId;
    }

    public void setDecideSnapId(Long decideSnapId) {
        this.decideSnapId = decideSnapId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getIsDelUser() {
        return isDelUser;
    }

    public void setIsDelUser(Integer isDelUser) {
        this.isDelUser = isDelUser;
    }

    public Integer getBlackUserLevel() {
        return blackUserLevel;
    }

    public void setBlackUserLevel(Integer blackUserLevel) {
        this.blackUserLevel = blackUserLevel;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

