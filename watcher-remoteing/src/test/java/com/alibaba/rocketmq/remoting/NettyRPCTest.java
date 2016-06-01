/**
 * $Id: NettyRPCTest.java 1831 2013-05-16 01:39:51Z shijia.wxr $
 */
package com.alibaba.rocketmq.remoting;

import static org.junit.Assert.assertTrue;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.alibaba.rocketmq.remoting.annotation.CFNullable;
import com.alibaba.rocketmq.remoting.exception.RemoteCommandException;
import com.alibaba.rocketmq.remoting.exception.RemoteConnectException;
import com.alibaba.rocketmq.remoting.exception.RemoteSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemoteTimeoutException;
import com.alibaba.rocketmq.remoting.exception.RemoteTooMuchRequestException;
import com.alibaba.rocketmq.remoting.netty.client.NettyClientConfig;
import com.alibaba.rocketmq.remoting.netty.client.NettyRemoteClient;
import com.alibaba.rocketmq.remoting.netty.server.NettyRemoteServer;
import com.alibaba.rocketmq.remoting.netty.NettyRequestProcessor;
import com.alibaba.rocketmq.remoting.netty.server.NettyServerConfig;
import com.alibaba.rocketmq.remoting.netty.ResponseFuture;
import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;


/**
 * @author shijia.wxr<vintage.wang@gmail.com>
 */
public class NettyRPCTest {
    public static RemoteClient createRemoteClient() {
        NettyClientConfig config = new NettyClientConfig();
        RemoteClient client = new NettyRemoteClient(config);
        client.start();
        return client;
    }


    public static RemoteServer createRemoteServer() throws InterruptedException {
        NettyServerConfig config = new NettyServerConfig();
        RemoteServer remoteServer = new NettyRemoteServer(config);
        remoteServer.registerProcessor(0, new NettyRequestProcessor() {
            private int i = 0;

            @Override
            public RemoteCommand processRequest(ChannelHandlerContext ctx, RemoteCommand request) {
//                System.out.println("processRequest=" + request + " " + (i++));
                request.setRemark("hello, I am respponse " + ctx.channel().remoteAddress());
                return request;
            }
        }, Executors.newCachedThreadPool());
        remoteServer.start();
        return remoteServer;
    }


    @Test
    public void test_RPC_Sync() throws InterruptedException, RemoteConnectException,
            RemoteSendRequestException, RemoteTimeoutException {
        RemoteServer server = createRemoteServer();
        RemoteClient client = createRemoteClient();

        for (int i = 0; i < 10; i++) {
            TestRequestHeader requestHeader = new TestRequestHeader();
            requestHeader.setCount(i);
            requestHeader.setMessageTitle("HelloMessageTitle");
            RemoteCommand request = RemoteCommand.createRequestCommand(0, requestHeader);

            RemoteCommand response = client.invokeSync("10.1.66.31:8888", request, 1000 * 3000);
            System.out.println("invoke result = " + response);
            assertTrue(response != null);
        }

        client.shutdown();
//        server.shutdown();
        System.out.println("-----------------------------------------------------------------");


        Thread.sleep(1000000L);
    }


    @Test
    public void test_RPC_Oneway() throws InterruptedException, RemoteConnectException,
            RemoteTimeoutException, RemoteTooMuchRequestException, RemoteSendRequestException {
        RemoteServer server = createRemoteServer();
        RemoteClient client = createRemoteClient();

        for (int i = 0; i < 100; i++) {
            RemoteCommand request = RemoteCommand.createRequestCommand(0, null);
            request.setRemark(String.valueOf(i));
            client.invokeOneway("localhost:8888", request, 1000 * 3);
        }

        client.shutdown();
        server.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }


    @Test
    public void test_RPC_Async() throws InterruptedException, RemoteConnectException,
            RemoteTimeoutException, RemoteTooMuchRequestException, RemoteSendRequestException {
        RemoteServer server = createRemoteServer();
        RemoteClient client = createRemoteClient();

        for (int i = 0; i < 100; i++) {
            RemoteCommand request = RemoteCommand.createRequestCommand(0, null);
            request.setRemark(String.valueOf(i));
            client.invokeAsync("localhost:8888", request, 1000 * 3, new InvokeCallback() {
                @Override
                public void operationComplete(ResponseFuture responseFuture) {
                    System.out.println(responseFuture.getResponseCommand());
                }
            });
        }

        Thread.sleep(1000 * 3);

        client.shutdown();
        server.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }


    @Test
    public void test_server_call_client() throws InterruptedException, RemoteConnectException,
            RemoteSendRequestException, RemoteTimeoutException {
        final RemoteServer server = createRemoteServer();
        final RemoteClient client = createRemoteClient();

        server.registerProcessor(0, new NettyRequestProcessor() {
            @Override
            public RemoteCommand processRequest(ChannelHandlerContext ctx, RemoteCommand request) {
                try {
                    return server.invokeSync(ctx.channel(), request, 1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteSendRequestException e) {
                    e.printStackTrace();
                } catch (RemoteTimeoutException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }, Executors.newCachedThreadPool());

        client.registerProcessor(0, new NettyRequestProcessor() {
            @Override
            public RemoteCommand processRequest(ChannelHandlerContext ctx, RemoteCommand request) {
                System.out.println("client receive server request = " + request);
                request.setRemark("client remark");
                return request;
            }
        }, Executors.newCachedThreadPool());

        for (int i = 0; i < 3; i++) {
            RemoteCommand request = RemoteCommand.createRequestCommand(0, null);
            RemoteCommand response = client.invokeSync("localhost:8888", request, 1000 * 3);
            System.out.println("invoke result = " + response);
            assertTrue(response != null);
        }

        client.shutdown();
        server.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }

}


class TestRequestHeader implements CommandCustomHeader {
    @CFNullable
    private Integer count;

    @CFNullable
    private String messageTitle;


    @Override
    public void checkFields() throws RemoteCommandException {
    }


    public Integer getCount() {
        return count;
    }


    public void setCount(Integer count) {
        this.count = count;
    }


    public String getMessageTitle() {
        return messageTitle;
    }


    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
}


class TestResponseHeader implements CommandCustomHeader {
    @CFNullable
    private Integer count;

    @CFNullable
    private String messageTitle;


    @Override
    public void checkFields() throws RemoteCommandException {

    }


    public Integer getCount() {
        return count;
    }


    public void setCount(Integer count) {
        this.count = count;
    }


    public String getMessageTitle() {
        return messageTitle;
    }


    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
}
