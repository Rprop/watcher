package com.whh.ump.profiler.util;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class LogFormatterTest {

    @Test
    public void test() {
        String p = "{{}fghfghfghf, {}fghfghfghfgh, {}fghfghfgh}";
        assertEquals("{afghfghfghf, 1fghfghfghfgh, 2fghfghfgh}", LogFormatter.format(p, "a", 1, "2"));
        
        String p2 = "{%sfghfghfghf, %sfghfghfghfgh, %sfghfghfgh}";
        Assert.assertEquals("{afghfghfghf, 1fghfghfghfgh, 2fghfghfgh}", String.format(p2, "a", 1, "2"));
    }

    
    @Test
    public void testPerf() {
        String p1 = "{{}fghfgfghfghfghfghfghfghfgfghfghfghfghf, {}fghfgdfdxgdgdfgdfgdfgdhfghfgh, {}fghfghfghghjfgjhghjghjghjghjghjg}";
        
        int num = 100;
        int num1 = num;
        long start1 = System.currentTimeMillis();
        while(num1-- > 0){
            LogFormatter.format(p1, "a", 1, "2");
        }

        System.out.println("LogFormatter:" + (System.currentTimeMillis() - start1));
        
        int num2 = num;
        long start2 = System.currentTimeMillis();
        while(num2-- > 0){
            String.format(p1, "a", 1, "2");
        }

        System.out.println("String.format:" + (System.currentTimeMillis() - start2));
        
        
        
        
    }
}
