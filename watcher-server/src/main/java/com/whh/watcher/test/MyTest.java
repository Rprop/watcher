package com.whh.watcher.test;

import com.alibaba.rocketmq.remoting.RemoteServer;
import com.alibaba.rocketmq.remoting.netty.server.NettyRemoteServer;
import com.alibaba.rocketmq.remoting.netty.server.NettyServerConfig;
import com.whh.watcher.receiver.MessageReceiver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Executors;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public class MyTest {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        MessageReceiver messageReceiver = context.getBean("messageReceiver", MessageReceiver.class);

        RemoteServer server = MyTest.createRemoteServer(messageReceiver);
        System.out.println(server + " is ok");
//        server.shutdown();
    }

    public static RemoteServer createRemoteServer(MessageReceiver receiver) throws InterruptedException {
        NettyServerConfig config = new NettyServerConfig();
        RemoteServer remoteServer = new NettyRemoteServer(config);
        remoteServer.registerProcessor(0, receiver, Executors.newCachedThreadPool());
        remoteServer.start();
        return remoteServer;
    }
}
