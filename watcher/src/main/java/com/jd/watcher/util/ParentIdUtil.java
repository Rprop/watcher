package com.jd.watcher.util;

/**
 * Dept：获取本次请求的上级ID
 * User:wanghanghang
 * Date:2016/5/12
 * Version:1.0
 */
public class ParentIdUtil {
    private static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal();
    private static ParentIdUtil instance = new ParentIdUtil();

    private static ParentIdUtil getInstance() {
        return instance;
    }

    public ParentIdUtil() {
    }

    public static String getParentId() {
        return threadLocal.get();
    }

    public static void setParentId(String id) {
        threadLocal.set(id);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
