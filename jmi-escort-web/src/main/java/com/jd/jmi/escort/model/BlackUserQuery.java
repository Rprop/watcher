package com.jd.jmi.escort.model;

import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.util.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by changpan on 2016/2/17.
 */
public class BlackUserQuery extends PageBaseQuery implements Serializable {


    private static final long serialVersionUID = 3836881414671157939L;

    private String userPin;
    private Date startTime;
    private Date endTime;

    private String startTimeStr;

    private String endTimeStr;
    private Integer orderType;

    private Integer level;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Date getStartTime() {
        if(startTime == null && StringUtils.isNotBlank(getStartTimeStr())){
            startTime = DateUtils.getDateTime(getStartTimeStr());
        }
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        if(endTime == null && StringUtils.isNotBlank(getEndTimeStr())){
            endTime = DateUtils.getDateTime(getEndTimeStr());
        }
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }
}
