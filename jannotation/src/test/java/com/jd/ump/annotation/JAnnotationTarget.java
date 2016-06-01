package com.jd.ump.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jd.ump.annotation.JProEnum;
import com.jd.ump.annotation.JProfiler;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;


@Component("ja")
public class JAnnotationTarget implements AnnotationTarget{
    /**
     * 方法说明：
     * 监控注解说明：为此方法加入tp性能监控，方法心跳(JProEnum.Heartbeat)，
     *              并且监控中需要捕获异常以此来监控方法是否执行成功(JProEnum.FunctionError)，
     *              最后同时初始化系统心跳(initHB = true)。
     * @throws Exception
     */
    @JProfiler(jKey = "ump.all.Parameter1", jAppName = "ump_JANOTATION1", mState = {JProEnum.TP,JProEnum.Heartbeat,JProEnum.FunctionError})  
    public void exec() throws Exception { 
        System.out.println("exec...");
        System.out.println("ump.all.Parameter");
        privateTest();
        protectedTest();
        throw new Exception("ump.all.Parameter");
       
    }  
    
    @JProfiler(jKey = "ump.all.Parameter2", jAppName = "ump_JANOTATION2", mState = {JProEnum.TP,JProEnum.Heartbeat,JProEnum.FunctionError})  
    public void testAutoLoad() throws Exception { 
        privateTest();
        protectedTest();
    }
    
    
    private void privateTest(){
        CallerInfo  callerInfo = Profiler.registerInfo("privateTest", false, true);
        System.out.println("privateTest...");
        Profiler.registerInfoEnd(callerInfo);
    }
    protected void protectedTest(){
        System.out.println("privateTest...");
    }
    /**
     * 方法说明：......
     * 监控注解说明：添加tp性能监控和方法心跳。
     */
    @JProfiler(jKey = "ump.TP.Heartbeat",
            mState = {JProEnum.TP,JProEnum.Heartbeat}
    )  
    public String execTpAndHeartbeat(){ 
        privateTest();
        protectedTest();
        System.out.println("exec...");
        System.out.println("execTpAndHeartbeat");
        return "ssss";
    }  
    
    /**
     * 方法说明：......
     * 监控注解说明：此注解默认产生tp性能监控的日志,默认值 mState = {JProEnum.TP}。
     */
    @JProfiler(jKey = "ump.execNull.tp")  
     public int execNull() { 
    
         System.out.println("exec...");
         System.out.println("ump.null.null.null");        
         return 10;
     }  
    /**
     * 监控注解说明：为此方法加入tp性能监控(JProEnum.TP)，
     * 并且监控中需要捕获异常以此来监控方法是否执行成功(JProEnum.FunctionError)，
     */
    @JProfiler(jKey = "ump.tp.error",
    		   mState = {JProEnum.TP,JProEnum.FunctionError}
    )  
    public void execError() { 
        System.out.println("exec...");
        System.out.println("ssssexecError"); 
        throw new NullPointerException("sssssssssssssssssssssssssssss");
    }  
    
    /**
     * 方法说明：......
     * 监控注解说明：此注解不会产生任何监控行为，不会生成监控日志。
     */
    @JProfiler(jKey="ump.map.List")
     public List<String> execNullParameter() { 
    
         System.out.println("exec...");
         System.out.println("execNullParameter");        
         List<String> list = new ArrayList<String>();
         list.add("哦哦斯蒂芬 ");
         return list;
     }  
    @JProfiler(jKey="ump.map")
    public Map<String,String> execMap() { 
   
        System.out.println("exec...");
        System.out.println("ump.map");        
        Map<String,String> map = new HashMap<String,String>();
        map.put("map", "mapTessxt");
        return map;
    }  
    @JProfiler(jKey="ump.void")
    public void execvoid() { 
   
        System.out.println("exec...");
        System.out.println("ump.void");        
        
    }  
}
