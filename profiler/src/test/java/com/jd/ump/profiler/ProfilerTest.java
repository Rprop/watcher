package com.jd.ump.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.jd.ump.profiler.proxy.Profiler;

public class ProfilerTest {
    private static Random rd = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        Profiler.InitHeartBeats("12312312313");
        Profiler.registerJVMInfo("345345345345");
        
        business();
        biz();
        
        List<Thread> threads = new ArrayList<Thread>();
        long start = System.currentTimeMillis();
        
        final int threadCount1 = 1;
        final long runCount1 = 200000000000L;
        
        final int threadCount2 = 200;
        final long runCount2 = 200000000000L;
        
//        for (int i = 1; i <= threadCount1; i++) {
//            final int k = i;
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    long n = runCount1;
//                    String key = "key"; 
//                    while (n-- > 0L){
//                        try {
//                            profile1(key , k);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    System.out.println("thread" + k + ", " + key + " is success.");
//                }
//            });
//
//            t.start();
//            threads.add(t);
//        }
        
        for (int i = 1; i <= threadCount2; i++) {
            final int k = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    long n = runCount2;
                    while (n-- > 0L){
                        try {
                            profile2("keyLarge");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("keyLarge" + k + " is success.");
                }
            });

            t.start();
            threads.add(t);
        }

        for(Thread t : threads){
            t.join();
        }
        
        System.out.println("time=" + (System.currentTimeMillis() - start));        
        System.out.println("all key is success, wait for write log");
        
        Thread.sleep(10 * 1000L);
        System.out.println("write (" + (threadCount1 * runCount1 * 2L + threadCount2 * runCount2 * 3L) + ") logs done.");
    }

    public static void profile1(String key, int maxTime) throws InterruptedException {
        CallerInfo callerInfo1 = Profiler.registerInfo(key, true, true);

        CallerInfo callerInfo2 = Profiler.registerInfo(key + "a", true, true);
//        int stop = rd.nextInt(5) - 3;
//        if(stop > 0){
//            Thread.sleep(stop);
//        }
        Profiler.registerInfoEnd(callerInfo2);

        Profiler.registerInfoEnd(callerInfo1);
    }
    
    public static void profile2(String key) throws InterruptedException {
        CallerInfo callerInfo1 = Profiler.registerInfo(key, true, true);
      int stop = rd.nextInt(10) - 8;
      if(stop > 0){
          Thread.sleep(stop);
      }
        //CallerInfo callerInfo2 = Profiler.registerInfo(key + "a2", true, true);
        biz();
        //Profiler.registerInfoEnd(callerInfo2);
        //Thread.sleep(rd.nextInt(500));
        //business();
        
        //common();

        Profiler.registerInfoEnd(callerInfo1);
    }
    
    public static void business(){
        Profiler.businessAlarm("business1", "aaaaaaa");
        Profiler.businessAlarm("business2", System.currentTimeMillis(), "bbbbbbbbbbbbbb");
        Profiler.businessAlarm("business3", "ccccccccc", "rtx1,rtx2,rtx3", "asd@123.com,sdfsdf@dsf.com", "123455,456456456,456465");
        Profiler.businessAlarm("business4", System.currentTimeMillis(), "dddddddd", "rtx1,rtx2,rtx3", "asd@123.com,sdfsdf@dsf.com", "123455,456456456,456465");
    }
    
    public static void biz(){
//        Profiler.countAccumulate("biz1");
//        
//        Map<String, Number> data = new HashMap<String, Number>();
//        data.put("dkey11", 123);
//        data.put("dkey12", 789);
//        Profiler.sourceDataByNum("biz2", data);
//        
//        Map<String, String> data2 = new HashMap<String, String>();
//        data2.put("dkey21", "aaa");
//        data2.put("dkey22", "bbb");
//        Profiler.sourceDataByStr("biz3", data2);
//        
//        Map<String, String> data3 = new HashMap<String, String>();
//        Profiler.sourceDataByStr("biz4", data3);
//        
//        Profiler.valueAccumulate("biz5", 10);
//        
//        Map<String, Number> data4 = new HashMap<String, Number>();
//        Profiler.sourceDataByNum("biz6", data4);
        
        Map<String, String> data5 = new HashMap<String, String>();
        data5.put("dkdfgdfgdfgd", "aaadrgdg");
        data5.put("dkdfgdfgdfgd6", "aaadrgdg");
        data5.put("dkdfgdfgd456fgd", "123");
        data5.put("dkdfgdfgdfgd56", "aaadgjgrgdg");
        data5.put("dkey22fghfgh8", "bbb");
        Profiler.bizNode("esrtgsdfgdertdfbgdfgdfgdfgdfgdfgdfgd", data5);
        
        //Profiler.bizNode("abcd", "{\"key\":\"value\"}");
    }
    
    public static void common(){
        Map<String, String> data5 = new HashMap<String, String>();
        data5.put("dkey21", "aaa");
        data5.put("dkey22", "bbb");
        Profiler.log("t2", data5);
        
        Profiler.log("t1", "{\"aa\":\"bb\"}");
    }
}
