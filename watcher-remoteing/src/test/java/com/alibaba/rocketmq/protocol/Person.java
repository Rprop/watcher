package com.alibaba.rocketmq.protocol;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public class Person {
    private String name;
    private String test;

    public Person(String name, String test) {
        this.name = name;
        this.test = test;
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", test='" + test + '\'' +
                '}';
    }
}
