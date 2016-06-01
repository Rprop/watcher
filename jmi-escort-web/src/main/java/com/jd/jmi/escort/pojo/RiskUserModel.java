package com.jd.jmi.escort.pojo;

import com.jd.jmi.escort.model.PageBaseQuery;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 风险用户模型
 * Created by changpan on 2015/12/29.
 */
public class RiskUserModel extends PageBaseQuery implements Serializable {

    private static final long serialVersionUID = -542587814470351813L;
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 用户模型名称
     */
    @NotNull
    private String modelName;

    /**
     * 订单类型
     */
    @NotNull
    private Integer orderType;

    /**
     * 风险用户定义规则
     */
    private String ruleData;

    /**
     * 类型 1普通用户 2 ip用户
     */
    private Integer type =1;

    /**
     * 状态 1启用 0禁用 -1删除
     */
    private Integer status;

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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getRuleData() {
        return ruleData;
    }

    public void setRuleData(String ruleData) {
        this.ruleData = ruleData == null ? null : ruleData.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSnapId() {
        if(snapId == null){
            return 0L;
        }
        return snapId;
    }

    public void setSnapId(Long snapId) {
        this.snapId = snapId;
    }
}
