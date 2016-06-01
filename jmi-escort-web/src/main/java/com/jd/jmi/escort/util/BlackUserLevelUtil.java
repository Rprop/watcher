package com.jd.jmi.escort.util;

import com.jd.jmi.escort.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/3/8
 */
public class BlackUserLevelUtil {
    private Logger logger = LoggerFactory.getLogger(BlackUserLevelUtil.class);

    private static LinkedHashMap<Integer,String> blackUserLevels = new LinkedHashMap<Integer, String>();

    public BlackUserLevelUtil(){
    }

    public BlackUserLevelUtil(String configs) {
        if(StringUtils.isNotBlank(configs)){
            String[] blackUserLevelArr =  configs.split(";");
            for(String blackUserLevelItem : blackUserLevelArr){
                if(StringUtils.isBlank(blackUserLevelItem)){
                    logger.error("风险用户等级配置错误,存在空配置，configs={}",configs);
                    continue;
                }
                String[] blackUserLevel = blackUserLevelItem.split(":");

                Integer code = StringUtils.parseInt(blackUserLevel[0]);
                if(code == null){
                    logger.error("风险用户等级配置错误code非整形，configs={}",configs);
                    continue;
                }
                blackUserLevels.put(code,blackUserLevel[1]);
            }
        }
    }

    /*
        * 获取风险用户等级值
        * @param type
        * @return
        */
    public static  String getName(int code){
        return blackUserLevels.get(code);
    }

    public LinkedHashMap<Integer,String> getBlackUserLevels(){
        return blackUserLevels;
    }
}
