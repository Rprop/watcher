/**
 * $Id: ExceptionTest.java 1831 2013-05-16 01:39:51Z shijia.wxr $
 */
package com.alibaba.rocketmq.remoting;

import static org.junit.Assert.assertTrue;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executors;

import org.junit.Test;

import com.alibaba.rocketmq.remoting.exception.RemoteConnectException;
import com.alibaba.rocketmq.remoting.exception.RemoteSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemoteTimeoutException;
import com.alibaba.rocketmq.remoting.netty.client.NettyClientConfig;
import com.alibaba.rocketmq.remoting.netty.client.NettyRemoteClient;
import com.alibaba.rocketmq.remoting.netty.server.NettyRemoteServer;
import com.alibaba.rocketmq.remoting.netty.NettyRequestProcessor;
import com.alibaba.rocketmq.remoting.netty.server.NettyServerConfig;
import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;


/**
 * @author shijia.wxr<vintage.wang@gmail.com>
 */
public class ExceptionTest {
    private static RemoteClient createRemotingClient() {
        NettyClientConfig config = new NettyClientConfig();
        RemoteClient client = new NettyRemoteClient(config);
        client.start();
        return client;
    }


    private static RemoteServer createRemotingServer() throws InterruptedException {
        NettyServerConfig config = new NettyServerConfig();
        RemoteServer client = new NettyRemoteServer(config);
        client.registerProcessor(0, new NettyRequestProcessor() {
            private int i = 0;


            @Override
            public RemoteCommand processRequest(ChannelHandlerContext ctx, RemoteCommand request) {
                System.out.println("processRequest=" + request + " " + (i++));
                request.setRemark("hello, I am respponse " + ctx.channel().remoteAddress());
                return request;
            }
        }, Executors.newCachedThreadPool());
        client.start();
        return client;
    }


    @Test
    public void test_CONNECT_EXCEPTION() {
        RemoteClient client = createRemotingClient();

        RemoteCommand request = RemoteCommand.createRequestCommand(0, null);
        RemoteCommand response = null;
        try {
            response = client.invokeSync("localhost:8888", request, 1000 * 3);
        }
        catch (RemoteConnectException e) {
            e.printStackTrace();
        }
        catch (RemoteSendRequestException e) {
            e.printStackTrace();
        }
        catch (RemoteTimeoutException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("invoke result = " + response);
        assertTrue(null == response);

        client.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }

}
