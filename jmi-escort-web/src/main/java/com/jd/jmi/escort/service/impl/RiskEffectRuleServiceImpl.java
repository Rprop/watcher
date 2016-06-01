package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.common.enums.JmiRecordStatusEnum;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.dao.RiskEffectRuleDao;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SnapTypeEnum;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskSnapshot;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskSnapshotService;
import com.jd.jmi.escort.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生效规则
 * Created by changpan on 2016/1/15.
 */
@Service("riskEffectRuleService")
public class RiskEffectRuleServiceImpl implements RiskEffectRuleService {

    private static final Logger logger = LoggerFactory.getLogger(RiskEffectRuleServiceImpl.class);

    @Resource
    private RiskEffectRuleDao riskEffectRuleDao;

    @Resource
    private JmiRiskRuleJsfService jmiRiskRuleJsfService;

    @Resource
    private RiskSnapshotService riskSnapshotService;


    @Override
    public RiskEffectRule getById(long id) {
        return riskEffectRuleDao.getById(id);
    }

    @Override
    public List<RiskEffectRule> getEnabledByOrderType(int orderType) {
        RiskEffectRule rule = new RiskEffectRule();
        rule.setStatus(JmiRecordStatusEnum.VALID.getCode());
        rule.setOrderType(orderType);
        return riskEffectRuleDao.getEnabledByOrderType(rule);
    }

    @Override
    public List<RiskEffectRule> getEnabledByOrderType(int orderType, Integer processType) {
        RiskEffectRule rule = new RiskEffectRule();
        rule.setStatus(JmiRecordStatusEnum.VALID.getCode());
        rule.setOrderType(orderType);
        rule.setProcessType(processType);
        return riskEffectRuleDao.getEnabledByOrderType(rule);
    }

    @Override
    public PaginatedList<RiskEffectRule> list(RiskEffectRule model) {
        return riskEffectRuleDao.list(model);
    }

    /**
     * 最大时间
     */
    private static final Date maxTime = new Date(253402185600000L);

    @Override
    public Result save(RiskEffectRule model) {
        Result result = new Result(false);
        try {
            model.setEffectiveDate(DateUtils.parse(model.getStartTime(), DateUtils.FULL_FOMAT, new Date()));
            model.setExpiredDate(DateUtils.parse(model.getEndTime(), DateUtils.FULL_FOMAT, maxTime));
            model.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());

            RiskSnapshot riskSnapshot = new RiskSnapshot(0L, SnapTypeEnum.EFFECT_RULE,
                    JSON.toJSONString(model), model.getModifyUser());
            long snapId = riskSnapshotService.add(riskSnapshot);
            if (snapId <= 0) {
                return result.setMes("生成快照时失败");
            }
            model.setSnapId(snapId);

            long id = riskEffectRuleDao.add(model);
            if (id > 0) {
                result.setSuccess(true);
                result.setMes("添加成功");
                jmiRiskRuleJsfService.deleteRiskCache(model.getOrderType());
                riskSnapshotService.updateRuleIdById(snapId, id);
            } else {
                result.setMes("添加失败");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("保存风险判定模型错误", e);
        }
        return result;
    }

    @Override
    public Result update(RiskEffectRule model) {
        Result result = new Result(false);
        try {
            RiskEffectRule decideModel = getById(model.getId());
            if (decideModel == null) {
                result.setMes("更新失败，风险判定模型不存在");
                return result;
            }
            logger.info("修改用户模型userModel={}", JSON.toJSON(decideModel));
            model.setEffectiveDate(DateUtils.parse(model.getStartTime(), DateUtils.FULL_FOMAT, decideModel.getEffectiveDate()));
            model.setExpiredDate(DateUtils.parse(model.getEndTime(), DateUtils.FULL_FOMAT, decideModel.getExpiredDate()));
            model.setStatus(decideModel.getStatus());

            RiskSnapshot riskSnapshot = new RiskSnapshot(model.getId(), SnapTypeEnum.EFFECT_RULE,
                    JSON.toJSONString(model), model.getModifyUser());
            long snapId = riskSnapshotService.add(riskSnapshot);
            if (snapId <= 0) {
                return result.setMes("生成快照时失败");
            }
            model.setSnapId(snapId);

            int r = riskEffectRuleDao.update(model);
            if (r == 1) {
                result.setSuccess(true);
                result.setMes("更新成功");
                jmiRiskRuleJsfService.deleteRiskCache(model.getOrderType());
            } else {
                result.setMes("更新失败");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("更新风险用户模型错误", e);
        }
        return result;
    }

    @Override
    public Result updateEnabled(long id, String uname) {
        Result result = new Result(false, "启用失败");
        RiskEffectRule model = getById(id);
        if (model == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("启用模型id={},uname={}", id, uname);
        if (model.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
            return result.setMes("已经启用");
        } else if (model.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            model.setModifyUser(uname);
            model.setStatus(JmiRecordStatusEnum.VALID.getCode());
            int r = riskEffectRuleDao.updateStatus(model);
            if (r == 1) {
                result.setSuccessAndMes(true, "启用成功");
                jmiRiskRuleJsfService.deleteRiskCache(model.getOrderType());
            }
        } else {
            return result.setMes("该模型不存在");
        }
        return result;
    }

    @Override
    public Result updateUnabled(long id, String uname) {
        Result result = new Result(false, "禁用失败");
        RiskEffectRule model = getById(id);
        if (model == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("禁用模型id={},uname={}", id, uname);
        if (model.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            return result.setMes("已经禁用");
        } else if (model.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
            model.setModifyUser(uname);
            model.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
            int r = riskEffectRuleDao.updateStatus(model);
            if (r == 1) {
                result.setSuccessAndMes(true, "禁用成功");
                jmiRiskRuleJsfService.deleteRiskCache(model.getOrderType());
            }
        } else {
            return result.setMes("该模型不存在");
        }
        return result;
    }

    @Override
    public Result delete(long id, String uname) {
        Result result = new Result(false, "删除失败");
        RiskEffectRule model = getById(id);
        if (model == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("删除模型id={},uname={}", id, uname);
        if (model.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            model.setModifyUser(uname);
            model.setStatus(JmiRecordStatusEnum.DELETE.getCode());
            int r = riskEffectRuleDao.updateStatus(model);
            if (r == 1) {
                result.setSuccessAndMes(true, "删除成功");
                jmiRiskRuleJsfService.deleteRiskCache(model.getOrderType());
            }
        } else {
            return result.setMes("只有禁用状态才成删除");
        }
        return result;
    }

    /**
     * 是否含有该判定模型的规则
     *
     * @param decideId
     * @return
     */
    @Override
    public boolean hasRuleByDecide(long decideId) {
        return riskEffectRuleDao.countByDecideId(decideId) > 0 ? true : false;
    }

    /**
     * 是否含有该用户模型的规则
     *
     * @param userModelId
     * @param orderType
     * @return
     */
    public boolean hasRuleByUserModel(long userModelId, int orderType) {
        List<RiskEffectRule> list = riskEffectRuleDao.getByOrderType(orderType);
        if (CollectionUtils.isNotEmpty(list)) {
            String id = "," + String.valueOf(userModelId) + ",";
            for (RiskEffectRule rule : list) {
                if (StringUtils.isNotBlank(rule.getUserModelIds()) && ("," + rule.getUserModelIds() + ",").indexOf(id) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

}
