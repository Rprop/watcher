package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.cache.JimDBUtils;
import com.jd.jmi.escort.common.constant.CacheConstants;
import com.jd.jmi.escort.common.enums.JmiRecordStatusEnum;
import com.jd.jmi.escort.dao.BlackSyncRuleDao;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.BlackSyncRule;
import com.jd.jmi.escort.service.BlackSyncRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/16
 */
@Service("blackSyncRuleService")
public class BlackSyncRuleServiceImpl implements BlackSyncRuleService {
    private static final Logger logger = LoggerFactory.getLogger(BlackSyncRuleServiceImpl.class);

    @Resource
    private BlackSyncRuleDao blackSyncRuleDao;
    @Resource
    private JimDBUtils jimDBClient;

    @Override
    public BlackSyncRule getById(long id) {
        if (id <= 0) {
            return null;
        }
        return blackSyncRuleDao.getById(id);
    }

    @Override
    public BlackSyncRule getEnabledByOrderType(int orderType) {
        String cacheKey = CacheConstants.BLACK_SYNC_RULE_KEY_PREFIX + orderType;
        String cacheBlackSyncRule = jimDBClient.get(cacheKey);
        BlackSyncRule syncRule = JSON.parseObject(cacheBlackSyncRule, BlackSyncRule.class);
        if (null == syncRule) {
            List<BlackSyncRule> syncRuleList = blackSyncRuleDao.getEnabledByOrderType(orderType);
            if (null != syncRuleList && syncRuleList.size() > 0) {
                jimDBClient.setEx(cacheKey, JSON.toJSONString(syncRuleList.get(0)), CacheConstants.BLACK_SYNC_RULE_EXPIRE);
                return syncRuleList.get(0);
            } else {
                return null;
            }
        } else {
            return syncRule;
        }
    }

    @Override
    public List<BlackSyncRule> getByOrderType(int orderType) {
        return blackSyncRuleDao.getByOrderType(orderType);
    }

    @Override
    public List<BlackSyncRule> getAll() {
        return blackSyncRuleDao.getAll();
    }

    @Override
    public Result save(BlackSyncRule syncRule) {
        Result result = new Result(false);
        try {
            syncRule.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
            int r = blackSyncRuleDao.add(syncRule);

            List<BlackSyncRule> oldSyncRuleList = blackSyncRuleDao.getByOrderType(syncRule.getOrderType());
            String cacheKey = CacheConstants.BLACK_SYNC_RULE_KEY_PREFIX + syncRule.getOrderType();
            jimDBClient.setEx(cacheKey, JSON.toJSONString(oldSyncRuleList.get(0)), CacheConstants.BLACK_SYNC_RULE_EXPIRE);
            if (r > 0) {
                result.setSuccess(true);
                result.setMes("添加成功");
            } else {
                result.setMes("添加失败");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("保存黑名单同步规则错误", e);
        }
        return result;
    }

    @Override
    public Result update(BlackSyncRule syncRule) {
        Result result = new Result(false);
        try {
            BlackSyncRule oldSyncRule = getById(syncRule.getId());
            if (oldSyncRule == null) {
                result.setMes("更新失败，黑名单同步规则不存在");
                return result;
            }
            logger.info("修改黑名单同步规则syncRule={}", JSON.toJSON(oldSyncRule));
            syncRule.setStatus(oldSyncRule.getStatus());
            int r = blackSyncRuleDao.update(syncRule);
            String cacheKey = CacheConstants.BLACK_SYNC_RULE_KEY_PREFIX + syncRule.getOrderType();
            jimDBClient.setEx(cacheKey, JSON.toJSONString(syncRule), CacheConstants.BLACK_SYNC_RULE_EXPIRE);
            if (r == 1) {
                result.setSuccess(true);
                result.setMes("更新成功");
            } else {
                result.setMes("更新失败");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("更新黑名单同步规则错误", e);
        }
        return result;
    }

    @Override
    public Result updateEnabled(long id, String uname) {
        Result result = new Result(false, "启用失败");
        try {
            BlackSyncRule syncRule = getById(id);
            logger.info("启用同步规则id={},uname={}", id, uname);
            if (syncRule.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
                return result.setMes("已经启用");
            } else if (syncRule.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
                syncRule.setModifyUser(uname);
                syncRule.setStatus(JmiRecordStatusEnum.VALID.getCode());
                int r = blackSyncRuleDao.updateStatus(syncRule);
                String cacheKey = CacheConstants.BLACK_SYNC_RULE_KEY_PREFIX + syncRule.getOrderType();
                jimDBClient.setEx(cacheKey, JSON.toJSONString(syncRule), CacheConstants.BLACK_SYNC_RULE_EXPIRE);
                if (r == 1) {
                    result.setSuccessAndMes(true, "启用成功");
                }
            } else {
                return result.setMes("该记录不存在");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("启用规则错误", e);
        }
        return result;
    }

    @Override
    public Result updateUnabled(long id, String uname) {
        Result result = new Result(false, "禁用失败");
        try {
            BlackSyncRule syncRule = getById(id);
            logger.info("禁用同步规则id={},uname={}", id, uname);
            if (syncRule.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
                return result.setMes("已经禁用");
            } else if (syncRule.getStatus() == JmiRecordStatusEnum.VALID.getCode()) {
                syncRule.setModifyUser(uname);
                syncRule.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
                int r = blackSyncRuleDao.updateStatus(syncRule);
                String cacheKey = CacheConstants.BLACK_SYNC_RULE_KEY_PREFIX + syncRule.getOrderType();
                jimDBClient.setEx(cacheKey, JSON.toJSONString(syncRule), CacheConstants.BLACK_SYNC_RULE_EXPIRE);
                if (r == 1) {
                    result.setSuccessAndMes(true, "禁用成功");
                }
            } else {
                return result.setMes("该同步规则不存在");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("禁用规则错误", e);
        }
        return result;
    }

    @Override
    public Result delete(long id, String uname) {
        Result result = new Result(false, "删除失败");
        try {
            BlackSyncRule syncRule = getById(id);
            logger.info("删除同步规则id={},uname={}", id, uname);
            if (syncRule.getStatus() == JmiRecordStatusEnum.IN_VALID.getCode()) {
                syncRule.setModifyUser(uname);
                syncRule.setStatus(JmiRecordStatusEnum.DELETE.getCode());
                int r = blackSyncRuleDao.updateStatus(syncRule);
                String cacheKey = CacheConstants.BLACK_SYNC_RULE_KEY_PREFIX + syncRule.getOrderType();
                jimDBClient.setEx(cacheKey, JSON.toJSONString(syncRule), CacheConstants.BLACK_SYNC_RULE_EXPIRE);
                if (r == 1) {
                    result.setSuccessAndMes(true, "删除成功");
                }
            } else {
                return result.setMes("只有禁用状态才成删除");
            }
        } catch (Exception e) {
            result.setMes("系统错误");
            logger.error("删除规则错误", e);
        }
        return result;
    }
}
