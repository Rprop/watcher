/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jd.watcher.domain.enums;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public enum CountTypeEnum {
    avg("j", "求平均值"),
    total("c", "计数器"),
    multiAvg("a", "多条件求均值或总合");

    String code;
    String name;

    CountTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
