package com.jd.jmi.escort.util;

import java.io.Serializable;

/**
 * UIM返回菜单
 * Created by xuyonggang on 15-03-25.
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = -658389774177582562L;
    private int id;   // 系统菜单资源ID,自动生成
    private String resCodeStr = ""; // 资源码
    private String resCodeName = "";   // 资源码名称
    private String name = ""; // 菜单名称
    private int parent; // 上级菜单ID,系统自动生成
    private String icon = "";    // class样式名称
    private String url = "";   // 菜单url地址
    private String isParent = "";  // 是否是父菜单(true/false)
    private String parentResCode = "";  // 上级菜单资源码
    private int sortSequence;  // 菜单排序值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResCodeStr() {
        return resCodeStr;
    }

    public void setResCodeStr(String resCodeStr) {
        this.resCodeStr = resCodeStr;
    }

    public String getResCodeName() {
        return resCodeName;
    }

    public void setResCodeName(String resCodeName) {
        this.resCodeName = resCodeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getParentResCode() {
        return parentResCode;
    }

    public void setParentResCode(String parentResCode) {
        this.parentResCode = parentResCode;
    }

    public int getSortSequence() {
        return sortSequence;
    }

    public void setSortSequence(int sortSequence) {
        this.sortSequence = sortSequence;
    }
}
