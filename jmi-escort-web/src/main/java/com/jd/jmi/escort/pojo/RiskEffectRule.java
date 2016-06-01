package com.jd.jmi.escort.pojo;

import com.jd.jmi.escort.model.PageBaseQuery;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 生效规则
 */
public class RiskEffectRule extends PageBaseQuery implements Serializable {

    private static final long serialVersionUID = 6112482364933646526L;
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 风险规则名称
     */
    @NotNull
    private String name;

    /**
     * 订单类型
     */
    @NotNull
    private Integer orderType;

    /**
     * 规则生效开始时间
     */
    private Date effectiveDate;

    /**
     * 规则生效结束时间
     */
    private Date expiredDate;

    /**
     * 风险程度指数
     */
    @NotNull
    private Integer level;

    /**
     * 风险规则优先级
     */
    @NotNull
    private Integer rulePri;

    /**
     * 流量覆盖地域,以逗号隔开
     */
    private String region;

    /**
     * 用户模型id，多个以逗号隔开
     */
    @NotNull
    private String userModelIds;

    /**
     * 判定模型id
     */
    @NotNull
    private Long decideId;


    /**
     * 生效执行规则 1:不能下单 2:不能使用快捷支付 3:不可执行 4:不能使用优惠券  5:不能使用京豆支付
     */
    @NotNull
    private Integer executeRule;

    /**
     * 生效提示语
     */
    private String effectTip;

    /**
     * 状态 1启用 0禁用 -1删除
     */
    private Integer status;

    /**
     * 试用类型 1正式  2试用
     */
    @NotNull
    private Integer releaseType = 1;
    /**
     * 流程类型 订单类型+流程码
     */
    private Integer processType;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date modified;

    /**
     * 修改用户名
     */
    private String modifyUser;

    private String startTime;

    private String endTime;

    /**
     * 快照id
     */
    private Long snapId = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRulePri() {
        return rulePri;
    }

    public void setRulePri(Integer rulePri) {
        this.rulePri = rulePri;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getUserModelIds() {
        if (userModelIds == null) {
            return "";
        }
        return userModelIds;
    }

    public void setUserModelIds(String userModelIds) {
        this.userModelIds = userModelIds == null ? null : userModelIds.trim();
    }

    public Long getDecideId() {
        if (decideId == null) {
            return 0L;
        }
        return decideId;
    }

    public void setDecideId(Long decideId) {
        this.decideId = decideId;
    }

    public Integer getExecuteRule() {
        return executeRule;
    }

    public void setExecuteRule(Integer executeRule) {
        this.executeRule = executeRule;
    }

    public String getEffectTip() {
        return effectTip;
    }

    public void setEffectTip(String effectTip) {
        this.effectTip = effectTip == null ? null : effectTip.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getReleaseType() {
        if (releaseType == null) {
            return 1;
        }
        return releaseType;
    }

    public void setReleaseType(Integer releaseType) {
        this.releaseType = releaseType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public Long getSnapId() {
        if (snapId == null) {
            return 0L;
        }
        return snapId;
    }

    public void setSnapId(Long snapId) {
        this.snapId = snapId;
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }
}