package com.jd.ump.annotation;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class main_exc {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext beanApp = new ClassPathXmlApplicationContext("applicationContext.xml");

        JAnnotationTarget aopTarget = (JAnnotationTarget) beanApp.getBean("ja");
        
        int count = 0;
        while (count  < 1000) {

            try {
                try{
                    aopTarget.exec();
                }catch (Exception e1) {
                    System.out.println("----------------exec error---------------:" + e1.toString());
                }
               String value = aopTarget.execTpAndHeartbeat();
               System.out.println("---------------value--------------:" + value);
               System.out.println("int:" + aopTarget.execNull());
               System.out.println( aopTarget.execNullParameter());
               System.out.println( aopTarget.execMap());
               aopTarget.execvoid();
               aopTarget.execError();
            } catch (Exception e1) {
                System.out.println("---------------execError-error---------------:" + e1.toString());
            }finally{
                Thread.sleep(500);
                count++;
            }
           
        }
    }
}
