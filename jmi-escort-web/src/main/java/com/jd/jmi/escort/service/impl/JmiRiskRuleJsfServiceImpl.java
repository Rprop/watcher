package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.cache.JimDBUtils;
import com.jd.jmi.escort.common.constant.CacheConstants;
import com.jd.jmi.escort.common.model.risk.ConditionVO;
import com.jd.jmi.escort.common.model.risk.RiskDecideVO;
import com.jd.jmi.escort.common.model.risk.RiskObjectVO;
import com.jd.jmi.escort.common.model.risk.RiskRuleVO;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.constants.UmpConstants;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskDecideModelService;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskUserModelService;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 风险规则内部jsf接口
 * Created by changpan on 2016/1/27.
 */
@Service("jmiRiskRuleJsfService")
public class JmiRiskRuleJsfServiceImpl implements JmiRiskRuleJsfService {

    private static final Logger logger = LoggerFactory.getLogger(JmiRiskRuleJsfServiceImpl.class);

    @Resource
    private RiskEffectRuleService riskEffectRuleService;

    @Resource
    private RiskUserModelService riskUserModelService;

    @Resource
    private RiskDecideModelService riskDecideModelService;

    @Resource
    private JimDBUtils jimDBClient;


    /**
     * 根据订单类型获取风险规则
     *
     * @param orderType
     * @param processType 流程类型
     * @return
     */
    @Override
    public List<RiskRuleVO> getRisksByOrderType(int orderType, Integer processType) {
        CallerInfo info = Profiler.registerInfo(UmpConstants.ESCORT_WEB_JMI_APP_NAME, UmpConstants.GET_RISKS_BY_ORDERTYPE, false, true);
        List<RiskRuleVO> list = new ArrayList<RiskRuleVO>();
        try {
            String cache = jimDBClient.hGet(CacheConstants.getRiskRuleKey(orderType), CacheConstants.getProcessKey(processType));
            if (StringUtils.isNotBlank(cache)) {
                return JSON.parseArray(cache, RiskRuleVO.class);
            }
            List<RiskEffectRule> effects = riskEffectRuleService.getEnabledByOrderType(orderType, processType);
            if (CollectionUtils.isNotEmpty(effects)) {
                for (RiskEffectRule effect : effects) {
                    RiskRuleVO riskRuleVO = JSON.parseObject(JSON.toJSONString(effect), RiskRuleVO.class);

                    /** 判定模型 */
                    RiskDecideModel riskDecideModel = riskDecideModelService.getById(effect.getDecideId());
                    if (riskDecideModel != null) {
                        RiskDecideVO decideVO = new RiskDecideVO();
                        decideVO.setId(riskDecideModel.getId());
                        decideVO.setDecideName(riskDecideModel.getDecideName());
                        decideVO.setSnapId(riskDecideModel.getSnapId());
                        decideVO.setConditionList(getConditionVOs(riskDecideModel.getRuleData()));
                        riskRuleVO.setDecideVO(decideVO);
                    }

                    List<RiskObjectVO> objectList = new ArrayList<RiskObjectVO>();
                    /** 用户模型*/
                    if (StringUtils.isNotBlank(effect.getUserModelIds())) {
                        String[] uids = effect.getUserModelIds().split(",");
                        for (String uid : uids) {
                            Integer id = StringUtils.parseInt(uid);
                            if (id != null && id > 0) {
                                RiskUserModel riskUserModel = riskUserModelService.getById(id);
                                if (riskUserModel != null) {
                                    RiskObjectVO riskObjectVO = JSON.parseObject(JSON.toJSONString(riskUserModel), RiskObjectVO.class);
                                    riskObjectVO.setConditionList(getConditionVOs(riskUserModel.getRuleData()));
                                    objectList.add(riskObjectVO);
                                }
                            }
                        }
                    }
                    riskRuleVO.setObjectList(objectList);
                    list.add(riskRuleVO);
                }
            }
            jimDBClient.hSet(CacheConstants.getRiskRuleKey(orderType), CacheConstants.getProcessKey(processType), JSON.toJSONString(list));
        } catch (Exception e) {
            logger.error("获取风险规则失败", e);
            Profiler.functionError(info);
        } finally {
            Profiler.registerInfoEnd(info);
        }
        return list;
    }

    /**
     * 删除规则缓存
     *
     * @param orderType
     */
    public boolean deleteRiskCache(int orderType) {
        try {
            return jimDBClient.expire(CacheConstants.getRiskRuleKey(orderType), 0);
        } catch (Exception e) {
            logger.error("删除规则缓存失败", e);
            return false;
        }
    }

    private List<ConditionVO> getConditionVOs(String json) {
        if (StringUtils.isEmpty(json)) {
            return new ArrayList<ConditionVO>();
        }
        return JSON.parseArray(json, ConditionVO.class);
    }
}
