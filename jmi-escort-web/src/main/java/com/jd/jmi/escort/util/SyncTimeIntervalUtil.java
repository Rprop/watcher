package com.jd.jmi.escort.util;

import com.jd.jmi.escort.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/16
 */
public class SyncTimeIntervalUtil {
    private Logger logger = LoggerFactory.getLogger(SyncTimeIntervalUtil.class);

    private static LinkedHashMap<Integer,String> timeIntervals = new LinkedHashMap<Integer, String>();

    public SyncTimeIntervalUtil(){
    }

    public SyncTimeIntervalUtil(String configs) {
        if(StringUtils.isNotBlank(configs)){
            String[] timeIntervalArr =  configs.split(";");
            for(String timeIntervalItem : timeIntervalArr){
                if(StringUtils.isBlank(timeIntervalItem)){
                    logger.error("时段配置错误,存在空配置，configs={}",configs);
                    continue;
                }
                String[] timeInterval = timeIntervalItem.split(":");

                Integer code = StringUtils.parseInt(timeInterval[0]);
                if(code == null){
                    logger.error("订单类型配置错误code非整形，configs={}",configs);
                    continue;
                }
                timeIntervals.put(code,timeInterval[1]);
            }
        }
    }

    /*
        * 获取时间段值
        * @param type
        * @return
        */
    public static  String getName(int code){
        return timeIntervals.get(code);
    }

    public LinkedHashMap<Integer,String> getTimeIntervals(){
        return timeIntervals;
    }
}
