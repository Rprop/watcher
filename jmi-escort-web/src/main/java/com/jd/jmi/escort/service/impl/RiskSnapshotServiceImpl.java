package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.common.enums.JmiActionEnum;
import com.jd.jmi.escort.dao.RiskSnapshotDao;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.enums.SnapTypeEnum;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskSnapshot;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskSnapshotService;
import com.jd.jmi.escort.util.ConditionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by changpan on 2016/2/25.
 */
@Service
public class RiskSnapshotServiceImpl implements RiskSnapshotService {

    private static final Logger logger = LoggerFactory.getLogger(RiskSnapshotServiceImpl.class);

    @Resource
    private RiskSnapshotDao riskSnapshotDao;

    @Override
    public long add(RiskSnapshot riskSnapshot) {
        if (riskSnapshot == null) {
            return 0;
        }
        try {
            return riskSnapshotDao.add(riskSnapshot);
        } catch (Exception e) {
            logger.error("添加快照失败data=" + JSON.toJSONString(riskSnapshot), e);
        }
        return 0;
    }

    @Override
    public RiskSnapshot getById(long id) {
        if (id <= 0) {
            return null;
        }
        try {
            return riskSnapshotDao.getById(id);
        } catch (Exception e) {
            logger.error("查询快照失败id=" + id, e);
        }
        return null;
    }

    @Override
    public int updateRuleIdById(long id, long ruleId) {
        try {
            return riskSnapshotDao.updateRuleIdById(id, ruleId);
        } catch (Exception e) {
            logger.error("修改规则id失败id=" + id, e);
        }
        return 0;
    }

    /**
     * 通过快照id获取当时的规则
     *
     * @param id
     * @return
     */
    @Override
    public RiskEffectRule getrRiskEffectRule(long id) {
        RiskSnapshot riskSnapshot = getById(id);
        try {
            if (riskSnapshot != null && riskSnapshot.getType() == SnapTypeEnum.EFFECT_RULE.getCode()) {
                RiskEffectRule riskEffectRule = JSON.parseObject(riskSnapshot.getDescription(), RiskEffectRule.class);
                riskEffectRule.setId(riskSnapshot.getRuleId());
                return riskEffectRule;
            }
        } catch (Exception e) {
            logger.error("获取生效规则失败id=" + id, e);
        }
        return null;
    }

    /**
     * 获取快照的白话文翻译
     *
     * @param id
     * @return
     */
    @Override
    public RiskSnapshot translateById(long id) {
        RiskSnapshot riskSnapshot = getById(id);
        try {
            if (riskSnapshot != null) {
                List<String> list = new ArrayList<String>();
                if (riskSnapshot.getType() == SnapTypeEnum.EFFECT_RULE.getCode()) {
                    RiskEffectRule riskEffectRule = JSON.parseObject(riskSnapshot.getDescription(), RiskEffectRule.class);
                    JmiActionEnum jmiActionEnum = JmiActionEnum.getActionEnum(riskEffectRule.getExecuteRule());
                    list.add(jmiActionEnum == null ? "执行规则不存在" : jmiActionEnum.getName());
                } else if (riskSnapshot.getType() == SnapTypeEnum.USER_MODEL.getCode()) {
                    RiskUserModel riskUserModel = JSON.parseObject(riskSnapshot.getDescription(), RiskUserModel.class);
                    List<ConditionModel> conditionModels = JSON.parseArray(riskUserModel.getRuleData(), ConditionModel.class);
                    list = ConditionUtil.translate(conditionModels);
                } else if (riskSnapshot.getType() == SnapTypeEnum.DECIDE_MODEK.getCode()) {
                    RiskDecideModel riskDecideModel = JSON.parseObject(riskSnapshot.getDescription(), RiskDecideModel.class);
                    List<ConditionModel> conditionModels = JSON.parseArray(riskDecideModel.getRuleData(), ConditionModel.class);
                    list = ConditionUtil.translate(conditionModels);
                }
                riskSnapshot.setDescribes(list);
            }
        } catch (Exception e) {
            logger.error("获取快照翻译失败", e);
        }
        return riskSnapshot;
    }

    /**
     * 批量获取快照的白话文翻译
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, RiskSnapshot> translateByIds(long... ids) {
        Map<Long, RiskSnapshot> map = new HashMap<Long, RiskSnapshot>(ids.length);
        for (long id : ids) {
            map.put(id, translateById(id));
        }
        return map;
    }
}
