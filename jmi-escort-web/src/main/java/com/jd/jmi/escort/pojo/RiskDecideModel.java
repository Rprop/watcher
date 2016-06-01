package com.jd.jmi.escort.pojo;

import com.jd.jmi.escort.model.PageBaseQuery;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class RiskDecideModel extends PageBaseQuery implements Serializable{

    private static final long serialVersionUID = -1071962094866758712L;
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 判定模型名称
     */
    @NotNull
    private String decideName;

    /**
     * 订单类型
     */
    @NotNull
    private Integer orderType;

    /**
     * 风险判定规则
     */
    private String ruleData;

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

    public String getDecideName() {
        return decideName;
    }

    public void setDecideName(String decideName) {
        this.decideName = decideName == null ? null : decideName.trim();
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

    public Long getSnapId() {
        if(snapId==null){
            return 0L;
        }
        return snapId;
    }

    public void setSnapId(Long snapId) {
        this.snapId = snapId;
    }
}