package com.alibaba.rocketmq.remoting;

import org.junit.Test;

import com.alibaba.rocketmq.remoting.exception.RemoteConnectException;
import com.alibaba.rocketmq.remoting.exception.RemoteSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemoteTimeoutException;
import com.alibaba.rocketmq.remoting.netty.client.NettyClientConfig;
import com.alibaba.rocketmq.remoting.netty.client.NettyRemoteClient;
import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;


/**
 * 连接超时测试
 * 
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2013-7-6
 */
public class NettyConnectionTest {
    public static RemoteClient createRemotingClient() {
        NettyClientConfig config = new NettyClientConfig();
        config.setClientChannelMaxIdleTimeSeconds(15);
        RemoteClient client = new NettyRemoteClient(config);
        client.start();
        return client;
    }


    @Test
    public void test_connect_timeout() throws InterruptedException, RemoteConnectException,
            RemoteSendRequestException, RemoteTimeoutException {
        RemoteClient client = createRemotingClient();

        for (int i = 0; i < 100; i++) {
            try {
                RemoteCommand request = RemoteCommand.createRequestCommand(0, null);
                RemoteCommand response = client.invokeSync("localhost:8888", request, 1000 * 3);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        client.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }
}
