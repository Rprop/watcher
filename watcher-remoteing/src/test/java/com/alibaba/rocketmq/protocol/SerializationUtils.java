package com.alibaba.rocketmq.protocol;

import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public class SerializationUtils {
    public final static Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);//todo 先设置为关闭，将常用的类注册进来
        kryo.setReferences(false);
        kryo.setMaxDepth(20);

        //常用类注册
        kryo.register(HashMap.class);
        kryo.register(LinkedHashMap.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);

        //注册常用业务类
        kryo.register(Person.class);
        kryo.register(RemoteCommand.class);
    }

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(256, 4096);
        output.setOutputStream(outputStream);
        try {
            kryo.writeClassAndObject(output, obj);
            return output.toBytes();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                }
            }
            if (null != output) {
                output.close();
                output = null;
            }
        }

        return null;
    }

    public static Object deserialize(byte[] bytes) {
        Input input = null;
        try {
            input = new Input(bytes, 0, bytes.length);
            return kryo.readClassAndObject(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != input) {
                input.close();
                input = null;
            }
        }

        return null;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> classOfT) {
        Input input = null;
        try {
            input = new Input(bytes, 0, bytes.length);
            return kryo.readObject(input, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != input) {
                input.close();
                input = null;
            }
        }
        return null;
    }




    public static void main(String[] args) {
        Long time = System.currentTimeMillis();

        RemoteCommand object = RemoteCommand.createResponseCommand(1, "asdfads");
//        Person object = new Person("中国", "asfafds");


        for (int i = 0; i < 1000000; i++) {
            byte[] temp = SerializationUtils.serialize(object);
//            System.out.println(temp.length);
//            Object obj = com.alibaba.rocketmq.remoting.protocol.SerializationUtils.deserialize(temp);
//            System.out.println(obj);
        }
        //2949
        //1814
        //1046
        //873
        //2157
//        for (int i = 0; i < 1000000; i++) {
//            String temp = JSON.toJSONString(new Person("中国", "asfafds"));
//            Object obj = JSON.parseObject(temp,Person.class);//.deserialize(temp);
////            System.out.println(obj);
//        }

        System.out.println(System.currentTimeMillis() - time);
    }
}