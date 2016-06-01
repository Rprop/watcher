package com.jd.jmi.escort.util;

import com.jd.jmi.escort.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * Created by changpan on 2016/1/4.
 */
public class OrderTypeUtil {

    private Logger logger = LoggerFactory.getLogger(OrderTypeUtil.class);

    private static LinkedHashMap<Integer,String> orderTypes = new LinkedHashMap<Integer, String>();

    public OrderTypeUtil(){

    }

    public OrderTypeUtil(String configs) {
        if(StringUtils.isNotBlank(configs)){
            String[] orderTypeArr =  configs.split(";");
            for(String orderTypeItem : orderTypeArr){
                if(StringUtils.isBlank(orderTypeItem)){
                    logger.error("订单类型配置错误,存在空配置，configs={}",configs);
                    continue;
                }
                String[] orderType = orderTypeItem.split(":");
                if(orderType.length!=2){
                    logger.error("订单类型配置错误length!=2，configs={}",configs);
                    continue;
                }
                Integer type = StringUtils.parseInt(orderType[0]);
                if(type == null){
                    logger.error("订单类型配置错误type非整形，configs={}",configs);
                    continue;
                }
                orderTypes.put(type,orderType[1]);
            }
        }
    }

    /*
        * 获取订单类型
        * @param type
        * @return
        */
    public static  String getOrderType(int type){
        return orderTypes.get(type);
    }

    public LinkedHashMap<Integer,String> getOrderTypes(){
        return orderTypes;
    }
}
