package com.alibaba.rocketmq.remoting;

import com.alibaba.rocketmq.remoting.protocol.RemoteCommand;

/**
 * 发送或接收消息之前的拦截器
 */
public interface RPCHook {
    /**
     * 发送消息之前拦截
     * @param remoteAddr
     * @param request
     */
    public void doBeforeRequest(final String remoteAddr, final RemoteCommand request);

    /**
     * 发送完成消息后拦截
     * @param remoteAddr
     * @param request
     * @param response
     */
    public void doAfterResponse(final String remoteAddr, final RemoteCommand request, final RemoteCommand response);
}
