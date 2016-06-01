package com.alibaba.rocketmq.remoting;

public interface RemoteService {
    public void start();


    public void shutdown();


    public void registerRPCHook(RPCHook rpcHook);
}
