package com.whh.ump.profiler.common;

import com.whh.ump.profiler.util.CustomLogger;

import java.util.TimerTask;

public class UpdateModule extends TimerTask {
    private final static String alive = "{\"key\":\"UMPAutoGenerateAliveKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"time\":\"19900101010101000\"}";
    private final static String biz = "{\"bTime\":\"19900101010101000\",\"logtype\":\"BIZ\",\"bKey\":\"UMPAutoGenerateBizKeyDoNotUseThisKey\",\"bHost\":\"UMPTESTHOST\",\"type\":\"1\",\"bValue\":\"0\"}";
    private final static String business = "{\"time\":\"19900101010101000\",\"key\":\"UMPAutoGenerateBusinessKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"type\":\"0\",\"value\":\"0\",\"detail\":\"\"}";
    private final static String tp = "{\"time\":\"19900101010101000\",\"key\":\"UMPAutoGenerateTpKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"processState\":\"0\",\"elapsedTime\":\"0\"}";
    private final static String common = "{\"time\":\"19900101010101000\",\"host\":\"UMPTESTHOST\",\"ip\":\"0.0.0.0\",\"iCode\":\"000000\",\"type\":\"TESTTYPE\",\"data\":{}}";

    @Override
    public void run() {
        try {
            CustomLogger.AliveLogger.info(alive);
            CustomLogger.BizLogger.info(biz);
            CustomLogger.BusinessLogger.info(business);
            CustomLogger.TpLogger.info(tp);
            CustomLogger.CommonLogger.info(common);
        } catch (Throwable e) {
        }
    }
}