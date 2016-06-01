package com.jd.ump.annotation;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;

import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;

/**
 *  
 * @title 监控注解实现
 * 实现方法tp监控
 * 方法心跳监控
 * 方法执行成功的判断
 * 初始化系统心跳
 * @author wjn,wenxing
 * @date 2012-12-20
 * @version v1.0
 */

@Aspect
public class JAnnotation implements InitializingBean{
    
    /**
     * 系统key,用于初始化系统心跳
     */
    private String systemKey;
    
    /**
     * jvm监控key,用于初始化jvm监控
     */
    private String jvmKey;
    
    /**
     * 应用名 
     */
    private String appName;
	/**
	 * 定义一个切入点,在方法含@JProfiler注解位置切入
	 */
	@Pointcut("@annotation(com.jd.ump.annotation.JProfiler)")
	public void JAnnotationPoint() {
	}

	/**
	 * 注解监控拦截处理方法
	 * @param jp
	 * @return
	 */
	@Around("JAnnotationPoint()")
	public Object execJAnnotation(ProceedingJoinPoint jp) throws Throwable {
	   
		Method method = getMethod(jp);
		
        //是否判断方法是否执行失败,默认不判断
        boolean functionerror = false;
		
		CallerInfo callerInfo = null;
		try {
			//拦截到的注解方法是JProfiler.class，并把定义的注解内容赋值
			JProfiler anno = method.getAnnotation(JProfiler.class);
			if (anno != null){
			  //取得监控key
	            final String jproKey = anno.jKey();
	            final String jproAppName = anno.jAppName();
	            if (!isBlank(jproKey)){
	                //是否监控方法性能,默认不监控
	                boolean tp = false;
	                //是否开启方法心跳,默认不监控
	                boolean heartbeat = false;
	              //取得监控范围
	                final JProEnum[] monitorState = anno.mState();
	                
	                //取得注解参数值
	                for (JProEnum me : monitorState) {
	                    if (me.equals(JProEnum.TP)) {
	                        tp = true;
	                    } else if (me.equals(JProEnum.Heartbeat)) {
	                        heartbeat = true;
	                    } else if (me.equals(JProEnum.FunctionError)) {
	                        functionerror = true;
	                    }
	                }
	                
                	if(!isBlank(jproAppName)){
	                	callerInfo = Profiler.registerInfo(jproKey, jproAppName, heartbeat, tp);
	                }else{
	                	if(!isBlank(appName)){
	  	                	callerInfo = Profiler.registerInfo(jproKey, appName, heartbeat, tp);
	  	                }else{
	  	                	callerInfo = Profiler.registerInfo(jproKey, heartbeat, tp);
	  	                }
	                }
	            }
			}
			
			//运行目标方法
		   return jp.proceed();
			
		} catch (Throwable e) {
		    //捕获目标方法的异常
			if (callerInfo != null && functionerror) {
			    //方法执行失败
				Profiler.functionError(callerInfo);
			}
			throw e;
		} finally {
			if (callerInfo != null) {
			    //方法执行结束
				Profiler.registerInfoEnd(callerInfo);
			}
		}
	}

	private Method getMethod(JoinPoint jp) throws Exception {
		MethodSignature msig = (MethodSignature) jp.getSignature();
		Method method = msig.getMethod();
		return method;
	}

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }
    /**
     * 初始化系统
     */
    public void afterPropertiesSet() throws Exception {
                //初始化系统心跳
        //如果指定了系统心跳名称就会自动初始化系统心跳
       if (!isBlank(systemKey)){
		   Profiler.InitHeartBeats(systemKey);
//    	   if(isBlank(appName)){
//    		   Profiler.InitHeartBeats(systemKey);
//    	   }else{//自动跑key
//    		   Profiler.InitHeartBeats(systemKey,appName);
//    	   }
       }
       //初始化jvm监控
       //如果指定了jvm监控名称就会自动初始化jvm监控
       if (!isBlank(jvmKey)){
		   Profiler.registerJVMInfo(jvmKey);
//    	   if(isBlank(appName)){
//    		   Profiler.registerJVMInfo(jvmKey);
//    	   }else{//自动跑key
//    		   Profiler.registerJVMInfo(jvmKey,appName);
//    	   }
       }
    }
    
    private boolean isBlank(String value){
    	if(null != value && !"".equals(value.trim())){
    		return false;
    	}
    	return true;
    }

    public void setJvmKey(String jvmKey) {
        this.jvmKey = jvmKey;
    }

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
