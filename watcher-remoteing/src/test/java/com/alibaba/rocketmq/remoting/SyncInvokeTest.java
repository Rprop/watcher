/**
 * $Id: SyncInvokeTest.java 1831 2013-05-16 01:39:51Z shijia.wxr $
 */
package com.alibaba.rocketmq.remoting;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;


/**
 * @author shijia.wxr<vintage.wang@gmail.com>
 */
public class SyncInvokeTest {
    @Test
    public void test_RPC_Sync() throws Exception {
        RemoteServer server = NettyRPCTest.createRemoteServer();
        RemoteClient client = NettyRPCTest.createRemoteClient();

        for (int i = 0; i < 100; i++) {
            try {
                RemoteCommand request = RemoteCommand.createRequestCommand(0, null);
                RemoteCommand response = client.invokeSync("localhost:8888", request, 1000 * 3);
                System.out.println(i + "\t" + "invoke result = " + response);
                assertTrue(response != null);
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        client.shutdown();
        server.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }
}
