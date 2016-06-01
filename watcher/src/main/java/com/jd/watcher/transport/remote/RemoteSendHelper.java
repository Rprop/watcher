package com.jd.watcher.transport.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.remoting.RemoteClient;
import com.alibaba.rocketmq.remoting.exception.RemoteConnectException;
import com.alibaba.rocketmq.remoting.exception.RemoteSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemoteTimeoutException;
import com.alibaba.rocketmq.remoting.exception.RemoteTooMuchRequestException;
import com.alibaba.rocketmq.remoting.netty.client.NettyClientConfig;
import com.alibaba.rocketmq.remoting.netty.client.NettyRemoteClient;
import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;
import com.jd.watcher.domain.Event;
import com.jd.watcher.config.ConfigFactory;
import com.jd.watcher.transport.remote.protocol.AddressPublishInfo;
import com.jd.watcher.transport.remote.protocol.ResponseCode;
import com.jd.watcher.transport.remote.protocol.SendMessageHeader;
import com.jd.watcher.spi.MessageCodec;
import com.jd.watcher.spi.impl.DefaultMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/4
 * Version:1.0
 */
public class RemoteSendHelper {
    private static Logger logger = LoggerFactory.getLogger(RemoteSendHelper.class);
    private static RemoteClient client;
    private final int maxMessageSize;
    private final int sendTimeOut;
    private final int retryTimes;
    private final AddressPublishInfo addressInfo;
    private final MessageCodec messageCodec = new DefaultMessageCodec();

    RemoteSendHelper() {
        NettyClientConfig config = new NettyClientConfig();
        client = new NettyRemoteClient(config);
        client.start();
        maxMessageSize = 1000;
        sendTimeOut = 100;
        retryTimes = 2;
        addressInfo = new AddressPublishInfo(ConfigFactory.getSystemConfig().getSvrAddress());
    }


    /**
     * 验证消息格式
     *
     * @param msg
     */
    public void checkMessage(String msg) {
        if (msg == null || msg.equals("")) {
            throw new RuntimeException("要发送的消息不能为空");
        }

        if (msg.length() > maxMessageSize) {
            throw new RuntimeException("消息大小超过最大值");
        }
    }


    private Boolean processSendResponse(final RemoteCommand response) {
        boolean result = false;
        switch (response.getCode()) {
            case ResponseCode.FLUSH_DISK_TIMEOUT:
            case ResponseCode.FLUSH_SLAVE_TIMEOUT:
            case ResponseCode.SLAVE_NOT_AVAILABLE: {
                // TODO LOG
            }
            case ResponseCode.SUCCESS: {
                switch (response.getCode()) {
                    case ResponseCode.FLUSH_DISK_TIMEOUT:
                        break;
                    case ResponseCode.FLUSH_SLAVE_TIMEOUT:
                        break;
                    case ResponseCode.SLAVE_NOT_AVAILABLE:
                        break;
                    case ResponseCode.SUCCESS:
                        result = true;
                        break;
                    default:
                        assert false;
                        break;
                }

            }
            default:
                break;
        }

        return result;
    }


    public Boolean sendEvent(String appName, List<Event> events, final long timeout) {
        final long maxTimeout = this.sendTimeOut + 1000;
        final long beginTimestamp = System.currentTimeMillis();
        long endTimestamp = beginTimestamp;
        int timesTotal = 1 + this.retryTimes;
        String[] brokersSent = new String[timesTotal];
        int times = 0;
        for (; times < timesTotal && (endTimestamp - beginTimestamp) < maxTimeout; times++) {
            String lastSendAddress = null;
            SendMessageHeader requestHeader = new SendMessageHeader();
            requestHeader.setAppName(appName);
            RemoteCommand request = RemoteCommand.createRequestCommand(0, requestHeader);
            request.setBody(messageCodec.encode(events));
            try {
                lastSendAddress = addressInfo.selectOneAddress(lastSendAddress);
//                RemoteCommand response = client.invokeSync(lastSendAddress, request, timeout);
                client.invokeAsync(lastSendAddress, request, timeout,null);
//                if (this.processSendResponse(response)) {
//                    return true;
//                }
                return true;
            } catch (RemoteConnectException e) {
                e.printStackTrace();
            } catch (RemoteSendRequestException e) {
                e.printStackTrace();
            } catch (RemoteTimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteTooMuchRequestException e) {
                e.printStackTrace();
            }
            brokersSent[times] = lastSendAddress;
            endTimestamp = System.currentTimeMillis();
        }

        logger.error("Send [{}] times, still failed, cost [{}]ms, BrokersSent: {}", times,
                (endTimestamp - beginTimestamp), Arrays.toString(brokersSent));
        return false;

    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("test", "123");
        map.put("key", "value");
        System.out.println(JSON.toJSONString(map));
        JSON.parseObject(JSON.toJSONString(map), Map.class);
    }
}
