package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.constants.UmpConstants;
import com.jd.jmi.escort.service.UserService;
import com.jd.jmi.exception.NoRetryException;
import com.jd.risk.riskservice.RiskInterface;
import com.jd.risk.riskservice.RiskRequest;
import com.jd.risk.riskservice.RiskRespons;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;
import com.jd.user.sdk.export.UserInfoExportService;
import com.jd.user.sdk.export.domain.UserBaseInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 用户信息
 * Created by changpan on 2016/2/23.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoExportService userInfoExportService;

    @Resource
    private RiskInterface riskInterface;

    @Resource(name = "userConfigMap")
    private HashMap<String, String> userConfigMap;

    //用户风险等级用到的配置信息key
    private static final String RISK_LEVEL_SYS_NAME_KEY = "risk_level_sys_name";
    private static final String RISK_LEVEL_USE_TYPE_KEY = "risk_level_use_type";

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 获取用户基本信息
     * @param userPin
     * @return
     */
    @Override
    public UserBaseInfoVo getUserinfo(String userPin){
        logger.info("查询会员等级：{}", userPin);
        CallerInfo info = Profiler.registerInfo(UmpConstants.WEB_QUERY_MEMBER_GRADE, UmpConstants.ESCORT_WEB_JMI_APP_NAME, false, true);
        try{
            UserBaseInfoVo baseInfoVo = userInfoExportService.getUserBaseInfoByPin(userPin, 1);
            logger.error("会员等级返回结果：{}", JSON.toJSONString(baseInfoVo));
            return baseInfoVo;
        } catch (Exception e) {
            logger.error("查询会员等级异常！,userPin ="+userPin, e);
            Profiler.functionError(info);
        } finally {
            Profiler.registerInfoEnd(info);
        }
        return null;
    }

    /**
     * 获取用户风险等级
     * @param userPin
     * @return
     */
    @Override
    public Double getRisk(String userPin){
        logger.info("获取会员等级：{}", userPin);
        CallerInfo info = Profiler.registerInfo(UmpConstants.WEB_QUERY_RISK_LEVEL, UmpConstants.ESCORT_WEB_JMI_APP_NAME, false, true);
        try{
            RiskRequest riskRequest = new RiskRequest();
            riskRequest.sysName = userConfigMap.get(RISK_LEVEL_SYS_NAME_KEY);
            riskRequest.useType = userConfigMap.get(RISK_LEVEL_USE_TYPE_KEY);
            riskRequest.pin = userPin;
            RiskRespons riskRespons = riskInterface.risk(riskRequest);
            if (riskRespons != null) {
                return riskRespons.getRiskValue();
            }
            throw new NoRetryException("用户风险等级查询接口返回结果为null");
        } catch (Exception e) {
            logger.error("风险等级查询userPin="+userPin, e);
            Profiler.functionError(info);
        } finally {
            Profiler.registerInfoEnd(info);
        }
        return null;
    }
}
