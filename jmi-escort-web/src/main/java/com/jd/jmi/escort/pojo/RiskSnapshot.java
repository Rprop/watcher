package com.jd.jmi.escort.pojo;

import com.jd.jmi.escort.model.enums.SnapTypeEnum;

import java.util.Date;
import java.util.List;

public class RiskSnapshot {
    /**
     * 自增主键
     */
    private Long id;

    private Long ruleId;

    private Integer type;

    /**
     * 描述
     */
    private List<String> describes;

    /**
     * 生效规则快照
     */
    private String description;

    /**
     * 插入时间
     */
    private Date created;

    /**
     * 修改用户名
     */
    private String modefyUser;

    public RiskSnapshot() {}

    public RiskSnapshot(Long ruleId, SnapTypeEnum snapTypeEnum, String description, String modefyUser) {
        this.ruleId = ruleId;
        this.type = snapTypeEnum.getCode();
        this.description = description;
        this.modefyUser = modefyUser;
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getModefyUser() {
        return modefyUser;
    }

    public void setModefyUser(String modefyUser) {
        this.modefyUser = modefyUser == null ? null : modefyUser.trim();
    }

    public List<String> getDescribes() {
        return describes;
    }

    public void setDescribes(List<String> describes) {
        this.describes = describes;
    }
}