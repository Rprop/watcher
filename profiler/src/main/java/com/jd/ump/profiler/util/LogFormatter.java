package com.jd.ump.profiler.util;


public class LogFormatter {
    public static String format(String messagePattern, Object... args) {
        return MessageFormatter.arrayFormat(messagePattern, args);
    }
}
