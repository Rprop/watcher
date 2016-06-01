package com.jd.jmi.escort.pojo;

import com.jd.jmi.escort.model.PageBaseQuery;

import java.io.Serializable;
import java.util.Date;

public class WhiteUser  extends PageBaseQuery implements Serializable {

    private static final long serialVersionUID = 7750886531637186528L;
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
     * 1:系统生成 2：后台录入
     */
    private Integer source;

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
        this.userPin = userPin == null ? null : userPin.trim();
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
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
}