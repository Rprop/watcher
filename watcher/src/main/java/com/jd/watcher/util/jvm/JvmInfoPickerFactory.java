package com.jd.watcher.util.jvm;

/**
 * JVM信息获取器的生产工厂
 *
 * @author chenhualiang
 * @since 2013-01-28
 */
public class JvmInfoPickerFactory {

    /**
     * 本地的JVM信息获取器 类型
     */
    public static final String PICKER_TYPE = "Local";

    /**
     * 创建 JVM信息获取器
     *
     * @return JvmInfoPicker 信息获取器
     * @author chenhualiang
     * @since 2013-01-28
     */
    public static JvmInfoPicker create(String type) {
        //默认返回本地的JVM信息获取器, 以后会根据情况增加remote类型
        return LocalJvmInfoPicker.getInstance();
    }

}
