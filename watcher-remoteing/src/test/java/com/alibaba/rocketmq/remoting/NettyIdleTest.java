package com.alibaba.rocketmq.remoting;

import static org.junit.Assert.assertTrue;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executors;

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
 * @since 2013-7-6
 */
public class NettyIdleTest {
    public static RemoteClient createRemotingClient() {
        NettyClientConfig config = new NettyClientConfig();
        config.setClientChannelMaxIdleTimeSeconds(15);
        RemoteClient client = new NettyRemoteClient(config);
        client.start();
        return client;
    }


    public static RemoteServer createRemotingServer() throws InterruptedException {
        NettyServerConfig config = new NettyServerConfig();
        config.setServerChannelMaxIdleTimeSeconds(30);
        RemoteServer remotingServer = new NettyRemoteServer(config);
        remotingServer.registerProcessor(0, new NettyRequestProcessor() {
            private int i = 0;


            @Override
            public RemoteCommand processRequest(ChannelHandlerContext ctx, RemoteCommand request) {
                System.out.println("processRequest=" + request + " " + (i++));
                request.setRemark("hello, I am respponse " + ctx.channel().remoteAddress());
                return request;
            }
        }, Executors.newCachedThreadPool());
        remotingServer.start();
        return remotingServer;
    }


    // @Test
    public void test_idle_event() throws InterruptedException, RemoteConnectException,
            RemoteSendRequestException, RemoteTimeoutException {
        RemoteServer server = createRemotingServer();
        RemoteClient client = createRemotingClient();

        for (int i = 0; i < 10; i++) {
            RemoteCommand request = RemoteCommand.createRequestCommand(0, null);
            RemoteCommand response = client.invokeSync("localhost:8888", request, 1000 * 3);
            System.out.println(i + " invoke result = " + response);
            assertTrue(response != null);

            Thread.sleep(1000 * 10);
        }

        Thread.sleep(1000 * 60);

        client.shutdown();
        server.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }

}
