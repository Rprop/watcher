package com.jd.ump.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @title Annotation
 * @author wjn
 * @date 2012-12-20
 * @version v1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface JProfiler {
    /**
     * 监控key，不能为空，没有默认值
     * @return
     */
    String jKey();
    /**
     * 应用名称，用于自动跑key用
     * @return
     */
    String jAppName() default "";
    /**
     * 监控点,默认监控点为tp性能监控
     * @return
     */
    JProEnum[] mState() default {JProEnum.TP};
}