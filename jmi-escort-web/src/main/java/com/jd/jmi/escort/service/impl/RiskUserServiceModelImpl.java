package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.common.enums.JmiRecordStatusEnum;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import com.jd.jmi.escort.condition.ConditionToolInterface;
import com.jd.jmi.escort.dao.RiskUserModelDao;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SnapTypeEnum;
import com.jd.jmi.escort.pojo.RiskSnapshot;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskSnapshotService;
import com.jd.jmi.escort.service.RiskUserModelService;
import com.jd.jmi.escort.util.ConditionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by changpan on 2015/12/28.
 */
@Service("riskUserModelService")
public class RiskUserServiceModelImpl implements RiskUserModelService {

    private static final Logger logger = LoggerFactory.getLogger(RiskUserServiceModelImpl.class);

    @Resource
    private RiskUserModelDao riskUserModelDao;

    @Resource
    private RiskEffectRuleService riskEffectRuleService;

    @Resource
    private JmiRiskRuleJsfService jmiRiskRuleJsfService;

    @Resource
    private RiskSnapshotService riskSnapshotService;


    @Override
    public RiskUserModel getById(long id) {
        return riskUserModelDao.getById(id);
    }

    @Override
    public List<RiskUserModel> getEnabledByOrderType(int orderType) {
        return riskUserModelDao.getEnabledByOrderType(orderType);
    }

    @Override
    public PaginatedList<RiskUserModel> list(RiskUserModel model) {
        return riskUserModelDao.list(model);
    }

    @Override
    public Result save(RiskUserModel riskUserModel, List<ConditionModel> conditionModels) {
        Result result = new Result(false);
        try {
            riskUserModel.setRuleData(JSON.toJSONString(conditionModels));
            riskUserModel.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
            RiskSnapshot riskSnapshot = new RiskSnapshot(0L, SnapTypeEnum.USER_MODEL,
                    JSON.toJSONString(riskUserModel), riskUserModel.getModifyUser());
            long snapId = riskSnapshotService.add(riskSnapshot);
            if (snapId <= 0) {
                return result.setMes("生成快照时失败");
            }
            riskUserModel.setSnapId(snapId);
            long id = riskUserModelDao.add(riskUserModel);
            if (id > 0) {
                result.setSuccess(true);
                result.setMes("添加成功");
                riskSnapshotService.updateRuleIdById(snapId, id);
            } else {
                result.setMes("添加失败");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("保存风险用户模型错误", e);
        }
        return result;
    }

    @Override
    public Result update(RiskUserModel riskUserModel, List<ConditionModel> conditionModels) {
        Result result = new Result(false);
        try {
            RiskUserModel userModel = getById(riskUserModel.getId());
            if (userModel == null) {
                result.setMes("更新失败，原风险用户模型不存在");
                return result;
            }
            logger.info("修改用户模型userModel={}", JSON.toJSON(userModel));

            riskUserModel.setRuleData(JSON.toJSONString(conditionModels));
            riskUserModel.setStatus(userModel.getStatus());
            RiskSnapshot riskSnapshot = new RiskSnapshot(riskUserModel.getId(), SnapTypeEnum.USER_MODEL,
                    JSON.toJSONString(riskUserModel), riskUserModel.getModifyUser());
            long snapId = riskSnapshotService.add(riskSnapshot);
            if (snapId <= 0) {
                return result.setMes("生成快照时失败");
            }
            riskUserModel.setSnapId(snapId);
            int r = riskUserModelDao.update(riskUserModel);
            if (r == 1) {
                result.setSuccess(true);
                result.setMes("更新成功");
                if (!jmiRiskRuleJsfService.deleteRiskCache(riskUserModel.getOrderType())) {
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
        RiskUserModel riskUserModel = getById(id);
        if (riskUserModel == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("启用模型id={},uname={}", id, uname);
        if (riskUserModel.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
            return result.setMes("已经启用");
        } else if (riskUserModel.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            riskUserModel.setModifyUser(uname);
            riskUserModel.setStatus(JmiRecordStatusEnum.VALID.getCode());
            int r = riskUserModelDao.updateStatus(riskUserModel);
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
        RiskUserModel riskUserModel = getById(id);
        if (riskUserModel == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("禁用模型id={},uname={}", id, uname);
        if (riskUserModel.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            return result.setMes("已经禁用");
        } else if (riskUserModel.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
            if (riskEffectRuleService.hasRuleByUserModel(id, riskUserModel.getOrderType())) {
                return result.setMes("该用户模型已经被使用，无法禁用");
            }
            riskUserModel.setModifyUser(uname);
            riskUserModel.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
            int r = riskUserModelDao.updateStatus(riskUserModel);
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
        RiskUserModel riskUserModel = getById(id);
        if (riskUserModel == null) {
            return result.setMes("该模型不存在");
        }
        logger.info("删除模型id={},uname={}", id, uname);
        if (riskUserModel.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
            if (riskEffectRuleService.hasRuleByUserModel(id, riskUserModel.getOrderType())) {
                return result.setMes("该用户模型已经被使用，无法删除");
            }
            riskUserModel.setModifyUser(uname);
            riskUserModel.setStatus(JmiRecordStatusEnum.DELETE.getCode());
            int r = riskUserModelDao.updateStatus(riskUserModel);
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
    public String translate(RiskUserModel model) {
        if (model == null) {
            return "";
        }
        try {
            List<ConditionModel> conditionModels = JSON.parseArray(model.getRuleData(), ConditionModel.class);
            List<String> values = ConditionUtil.translate(conditionModels);
            return JSON.toJSONString(values);
        } catch (Exception e) {
            logger.error("用户模型翻译错误id=" + model.getId(), e);
        }
        return "";
    }
}
