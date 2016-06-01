package com.jd.jmi.escort.pojo;

import java.util.Date;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/16
 */
public class BlackSyncRule {
    /**
     * 自增主键
     */
    private Long id;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 事件时段 单位天
     */
    private Integer timeInterval;
    /**
     * 触发规则次数
     */
    private Integer num;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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
