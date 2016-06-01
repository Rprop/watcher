package com.jd.jmi.escort.pojo;

import com.jd.jmi.escort.model.PageBaseQuery;

import java.io.Serializable;
import java.util.Date;

public class BlackUser extends PageBaseQuery implements Serializable{

    private static final long serialVersionUID = -4217995325740470749L;
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userPin;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 风险等级 1 风险用户 2 黑名单用户
     */
    private Integer level;

    /**
     * 触发次数
     */
    private Long triggerCount;

    /**
     * 1:系统生成 2：后台录入
     */
    private Integer source;

    /**
     * RCS同步次数
     */
    private Integer syncNum;

    /**
     * RCS同步时间
     */
    private Integer syncTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 插入时间
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(Long triggerCount) {
        this.triggerCount = triggerCount;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getSyncNum() {
        return syncNum;
    }

    public void setSyncNum(Integer syncNum) {
        this.syncNum = syncNum;
    }

    public Integer getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Integer syncTime) {
        this.syncTime = syncTime;
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
        this.modifyUser = modifyUser;
    }
}