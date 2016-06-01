package com.jd.watcher.domain;

import com.alibaba.fastjson.JSON;
import com.jd.watcher.domain.enums.EventTypeEnum;
import com.jd.watcher.domain.enums.JvmTypeEnum;
import com.jd.watcher.util.jvm.JvmInfoPicker;
import com.jd.watcher.util.jvm.JvmInfoPickerFactory;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class JvmEvent extends Event {
    private String data;
    private JvmTypeEnum type;
    private static JvmInfoPicker localJvm = JvmInfoPickerFactory.create(JvmInfoPickerFactory.PICKER_TYPE);

    public JvmEvent() {
    }

    private JvmEvent(String key, JvmTypeEnum type, String data) {
        this.event = EventTypeEnum.jvm;
        this.key = key;
        this.type = type;
        this.data = data;
    }

    /**
     * 发送运行时JVM信息
     *
     * @param jvmKey
     */
    public static void runTimeEvent(String jvmKey) {
        JvmEvent jvmEvent = new JvmEvent(jvmKey, JvmTypeEnum.runTimeInfo, localJvm.pickJvmRumtimeInfo());
        jvmEvent.end();
    }

    /**
     * 发送运行环境信息
     *
     * @param jvmKey
     */
    public static void jvmHandle(String jvmKey) {
        JvmEvent jvmEvent = new JvmEvent(jvmKey, JvmTypeEnum.envInfo, localJvm.pickJvmEnvironmentInfo());
        jvmEvent.end();
    }
    @Override
    public String toJsonString() {
        return "J" +JSON.toJSONString(this);
    }

    public String getData() {
        return data;
    }

    public JvmTypeEnum getType() {
        return type;
    }
}
