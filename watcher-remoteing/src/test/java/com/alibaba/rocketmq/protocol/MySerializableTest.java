package com.alibaba.rocketmq.protocol;

import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/4/29
 * Version:1.0
 */
public class MySerializableTest {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        setSerializableObject();
        System.out.println("Kryo Serializable writeObject time:" + (System.currentTimeMillis() - start) + " ms");
        start = System.currentTimeMillis();
        getSerializableObject();
        System.out.println("Kryo Serializable readObject time:" + (System.currentTimeMillis() - start) + " ms");

    }

    public static void setSerializableObject() throws FileNotFoundException {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        Output output = new Output(new FileOutputStream("file.bin"));


        for (int i = 0; i < 1000000; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("zhang0", i);
            map.put("zhang1", i);

            RemoteCommand command = RemoteCommand.createResponseCommand(1, "asdfads");

            kryo.writeObject(output, command);
        }
        output.flush();
        output.close();

    }


    public static void getSerializableObject() {
        Kryo kryo = new Kryo();

        kryo.setReferences(false);

        kryo.setRegistrationRequired(false);

        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        Input input;
        try {
            input = new Input(new FileInputStream("file.bin"));
            RemoteCommand simple = null;
            while ((simple = kryo.readObject(input, RemoteCommand.class)) != null) {
//                System.out.println(simple.toString());
//                System.out.println(simple.getCode()+ "  " + simple.getVersion()+ "  " + simple.getFlag());
            }

            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KryoException e) {

        }
    }
}

