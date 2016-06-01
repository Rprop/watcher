package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.common.enums.JmiRecordStatusEnum;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import com.jd.jmi.escort.condition.ConditionToolInterface;
import com.jd.jmi.escort.dao.RiskDecideModelDao;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SnapTypeEnum;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskSnapshot;
import com.jd.jmi.escort.service.RiskDecideModelService;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskSnapshotService;
import com.jd.jmi.escort.util.ConditionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 风险判定模型service
 * Created by changpan on 2016/1/11.
 */
@Service("riskDecideModelService")
public class RiskDecideModelServiceImpl implements RiskDecideModelService {

    private static final Logger logger = LoggerFactory.getLogger(RiskUserServiceModelImpl.class);

    @Resource
    private RiskDecideModelDao riskDecideModelDao;

    @Resource
    private RiskEffectRuleService riskEffectRuleService;

    @Resource
    private JmiRiskRuleJsfService jmiRiskRuleJsfService;

    @Resource
    private RiskSnapshotService riskSnapshotService;


    @Override
    public RiskDecideModel getById(long id) {
        if (id <= 0) {
            return null;
        }
        return riskDecideModelDao.getById(id);
    }

    @Override
    public List<RiskDecideModel> getEnabledByOrderType(int orderType) {
        return riskDecideModelDao.getEnabledByOrderType(orderType);
    }

    @Override
    public PaginatedList<RiskDecideModel> list(RiskDecideModel model) {
        return riskDecideModelDao.list(model);
    }

    @Override
    public Result save(RiskDecideModel model, List<ConditionModel> conditionModels) {
        Result result = new Result(false);
        try {
            model.setRuleData(JSON.toJSONString(conditionModels));
            model.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());

            RiskSnapshot riskSnapshot = new RiskSnapshot(0L, SnapTypeEnum.DECIDE_MODEK,
                    JSON.toJSONString(model), model.getModifyUser());
            long snapId = riskSnapshotService.add(riskSnapshot);
            if (snapId <= 0) {
                return result.setMes("生成快照时失败");
            }
            model.setSnapId(snapId);

            long id = riskDecideModelDao.add(model);
            if (id > 0) {
                result.setSuccess(true);
                result.setMes("添加成功");
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
    public Result update(RiskDecideModel model, List<ConditionModel> conditionModels) {
        Result result = new Result(false);
        try {
            RiskDecideModel decideModel = getById(model.getId());
            if (decideModel == null) {
                result.setMes("更新失败，风险判定模型不存在");
                return result;
            }
            logger.info("修改用户模型userModel={}", JSON.toJSON(decideModel));

            model.setRuleData(JSON.toJSONString(conditionModels));
            model.setStatus(decideModel.getStatus());

            RiskSnapshot riskSnapshot = new RiskSnapshot(decideModel.getId(), SnapTypeEnum.DECIDE_MODEK,
                    JSON.toJSONString(model), model.getModifyUser());
            long snapId = riskSnapshotService.add(riskSnapshot);
            if (snapId <= 0) {
                return result.setMes("生成快照时失败");
            }
            model.setSnapId(snapId);

            int r = riskDecideModelDao.update(model);
            if (r == 1) {
                result.setSuccess(true);
                result.setMes("更新成功");
                if (!jmiRiskRuleJsfService.deleteRiskCache(model.getOrderType())) {
                    result.setMes("更新成功,单更新缓存失败");
                }
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
        RiskDecideModel model = getById(id);
        if (model == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("启用模型id={},uname={}", id, uname);
        if (model.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
            return result.setMes("已经启用");
        } else if (model.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            model.setModifyUser(uname);
            model.setStatus(JmiRecordStatusEnum.VALID.getCode());
            int r = riskDecideModelDao.updateStatus(model);
            if (r == 1) {
                result.setSuccessAndMes(true, "启用成功");
            }
        } else {
            return result.setMes("该模型不存在");
        }
        return result;
    }

    @Override
    public Result updateUnabled(long id, String uname) {
        Result result = new Result(false, "禁用失败");
        RiskDecideModel model = getById(id);
        if (model == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("禁用模型id={},uname={}", id, uname);
        if (model.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            return result.setMes("已经禁用");
        } else if (model.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
            if (riskEffectRuleService.hasRuleByDecide(id)) {
                return result.setMes("该判定模型已经被使用，无法禁用");
            }
            model.setModifyUser(uname);
            model.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
            int r = riskDecideModelDao.updateStatus(model);
            if (r == 1) {
                result.setSuccessAndMes(true, "禁用成功");
            }
        } else {
            return result.setMes("该模型不存在");
        }
        return result;
    }

    @Override
    public Result delete(long id, String uname) {
        Result result = new Result(false, "删除失败");
        RiskDecideModel model = getById(id);
        if (model == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("删除模型id={},uname={}", id, uname);
        if (model.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            if (riskEffectRuleService.hasRuleByDecide(id)) {
                return result.setMes("该判定模型已经被使用，无法删除");
            }

            model.setModifyUser(uname);
            model.setStatus(JmiRecordStatusEnum.DELETE.getCode());
            int r = riskDecideModelDao.updateStatus(model);
            if (r == 1) {
                result.setSuccessAndMes(true, "删除成功");
            }
        } else {
            return result.setMes("只有禁用状态才成删除");
        }
        return result;
    }


    @Override
    public String translate(long id) {
        return this.translate(getById(id));
    }

    @Override
    public String translate(RiskDecideModel model) {
        if (model == null) {
            return "";
        }
        try {
            List<ConditionModel> conditionModels = JSON.parseArray(model.getRuleData(), ConditionModel.class);
            List<String> values = ConditionUtil.translate(conditionModels);
            return JSON.toJSONString(values);
        } catch (Exception e) {
            logger.error("判定模型翻译错误id=" + model.getId(), e);
        }
        return "";
    }
}
