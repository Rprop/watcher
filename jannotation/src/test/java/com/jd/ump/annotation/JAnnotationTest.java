package com.jd.ump.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JAnnotationTest {
    static JAnnotationTarget aopTarget;
    /**
     * 加载applicationContext.xml文件，初始化<br>
     * 
     * 
     */
    @BeforeClass 
    public static void testInit(){
        ClassPathXmlApplicationContext beanApp = new ClassPathXmlApplicationContext("applicationContext.xml");
        aopTarget = (JAnnotationTarget) beanApp.getBean("ja");
    }
    

    /**
     * @JProfiler(jKey = "ump.xxxxx.yyyyy.nnnnn",
               mState = {JProEnum.TP,JProEnum.Heartbeat,JProEnum.FunctionError}
    )<br>
     * 设置key、系统心跳、方法性能（TP、方法心跳、方法异常（需要被监控的方法抛出异常））<br>
     * 抛出异常后会在调用该方法的地方获取到异常，并把日志中的方法成功率记为不成功。
     * @throws Exception 
     */
    @Test(expected = Exception.class)
    public void testA() throws Exception {
        aopTarget.exec();
    }
    
    /**
     * @JProfiler(jproKey="ump.null.null.null")<br>
     * 只有设置key，不设置任何监控点，默认产生tp性能监控日志
     */
    @Test
    public void execNull(){
        aopTarget.execNull();
    }
    /**
     * @JProfiler(jKey="")<br>
     * 设置key为空值不产生监控日志
     */
    @Test
    public void execNullParameter(){
        aopTarget.execNullParameter();
    }
    
    /**
     * @JProfiler(jKey = "ump.xxxxx.yyyyy.nnnnn",
            mState = {JProEnum.TP,JProEnum.Heartbeat}<br>
     * 设置key及方法心跳
     * 
     */
    @Test
    public void execTpAndHeartbeat() {
        aopTarget.execTpAndHeartbeat();
    }
    /**
     * @JProfiler(jKey = "ump.error.error.error",
               mState = {JProEnum.TP,JProEnum.FunctionError}
    )<br>
     * 捕获Error异常
     * 
     */
    @Test(expected=Throwable.class)
    public void testD() throws Throwable {
        //Thread.sleep(25000);
        aopTarget.execError();
    }
}
