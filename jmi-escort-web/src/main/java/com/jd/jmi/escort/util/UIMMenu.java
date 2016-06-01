package com.jd.jmi.escort.util;

import java.io.Serializable;
import java.util.List;

/**
 * UIM返回结构体
 * Created by xuyonggang on 15-03-25.
 */
public class UIMMenu implements Serializable {

    private static final long serialVersionUID = -8795938642702884088L;
    private List<Menu> menus;
    private Integer totalCount;  // 返回系统总数
    private String resStatus = "";   // 响应状态码(200:成功,500:失败/未查询到数据,400:API调用发生异常)
    private String resMsg = "";    // 响应提示信息
    private int resCount;// 返回的响应数据行数

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getResStatus() {
        return resStatus;
    }

    public void setResStatus(String resStatus) {
        this.resStatus = resStatus;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public int getResCount() {
        return resCount;
    }

    public void setResCount(int resCount) {
        this.resCount = resCount;
    }
}
