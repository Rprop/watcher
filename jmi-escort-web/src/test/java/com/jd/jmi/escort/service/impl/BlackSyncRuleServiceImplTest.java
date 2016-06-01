package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.cache.JimDBUtils;
import com.jd.jmi.escort.common.enums.JmiRecordStatusEnum;
import com.jd.jmi.escort.dao.BlackSyncRuleDao;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.BlackSyncRule;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/18
 */
public class BlackSyncRuleServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackSyncRuleServiceImplTest.class);

    @Tested
    private BlackSyncRuleServiceImpl blackSyncRuleService;
    @Injectable
    private BlackSyncRuleDao blackSyncRuleDao;
    @Injectable
    private JimDBUtils jimDBClient;
    /**
     * 保存
     *
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception {
        final BlackSyncRule syncRule = new BlackSyncRule();
        syncRule.setOrderType(47);
        syncRule.setTimeInterval(1);
        syncRule.setNum(3);
        syncRule.setModifyUser("uname");

        final List<BlackSyncRule> oldSyncRuleList = new ArrayList<BlackSyncRule>();
        oldSyncRuleList.add(syncRule);

        final int r = 1;
        new Expectations() {{
            blackSyncRuleDao.add((BlackSyncRule)any);
            result = r;
            blackSyncRuleDao.getByOrderType(syncRule.getOrderType());
            result = oldSyncRuleList;
            jimDBClient.setEx(anyString, anyString, anyInt);
            result = true;
        }};
        Result result = blackSyncRuleService.save(syncRule);
        Assert.assertTrue(result.isSuccess());
    }
    /**
     * 修改
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        final BlackSyncRule syncRule = new BlackSyncRule();
        syncRule.setId(21L);
        final int r = 1;
        new Expectations() {{
            blackSyncRuleDao.getById(anyLong);
            result = syncRule;
            blackSyncRuleDao.update((BlackSyncRule)any);
            result = r;
            jimDBClient.setEx(anyString, anyString, anyInt);
            result = true;
        }};
        Result result = blackSyncRuleService.update(syncRule);
        Assert.assertTrue(result.isSuccess());
    }
    /**
     * 启用
     *
     * @throws Exception
     */
    @Test
    public void testUpdateEnabled() throws Exception {
        final BlackSyncRule syncRule = new BlackSyncRule();
        syncRule.setId(21L);
        syncRule.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
        final int r = 1;
        new Expectations() {{
            blackSyncRuleDao.getById(anyLong);
            result = syncRule;
            blackSyncRuleDao.updateStatus((BlackSyncRule) any);
            result = r;
            jimDBClient.setEx(anyString, anyString, anyInt);
            result = true;
        }};
        Result result = blackSyncRuleService.updateEnabled(21, "uname");
        Assert.assertTrue(result.isSuccess());
    }
    /**
     * 禁用
     *
     * @throws Exception
     */
    @Test
    public void testUpdateUnabled() throws Exception {
        final BlackSyncRule syncRule = new BlackSyncRule();
        syncRule.setId(21L);
        syncRule.setStatus(JmiRecordStatusEnum.VALID.getCode());
        final int r = 1;
        new Expectations() {{
            blackSyncRuleDao.getById(anyLong);
            result = syncRule;
            blackSyncRuleDao.updateStatus((BlackSyncRule) any);
            result = r;
            jimDBClient.setEx(anyString, anyString, anyInt);
            result = true;
        }};

        Result result = blackSyncRuleService.updateUnabled(21, "uname");
        Assert.assertTrue(result.isSuccess());
    }
    /**
     * 根据类型查询有效数据
     *
     * @throws Exception
     */
    @Test
    public void testGetEnabledByOrderType() throws Exception {
        final List<BlackSyncRule> syncRuleList = new ArrayList<BlackSyncRule>();
        final BlackSyncRule rule = new BlackSyncRule();
        rule.setOrderType(47);
        syncRuleList.add(rule);

        final int r = 1;
        new Expectations() {{
            jimDBClient.get(anyString);
            result = null;
            blackSyncRuleDao.getEnabledByOrderType(47);
            result = rule;
            jimDBClient.setEx(anyString, anyString, anyInt);
            result = true;
        }};

        BlackSyncRule syncRule = blackSyncRuleService.getEnabledByOrderType(47);
        System.out.println("syncRule==" + JSON.toJSONString(syncRule));
    }
    /**
     * 根据ID查询数据
     *
     * @throws Exception
     */
    @Test
    public void testGetById() throws Exception {
        final BlackSyncRule rule = new BlackSyncRule();
        rule.setId(48L);
        new Expectations() {{
            blackSyncRuleDao.getById(anyLong);
            result = rule;
        }};
        BlackSyncRule syncRule = blackSyncRuleService.getById(rule.getId());
        Assert.assertEquals(rule.getId().longValue(), syncRule.getId().longValue());
    }
    /**
     * 查询所有有效数据
     *
     * @throws Exception
     */
    @Test
    public void testGetAll() throws Exception {
        final List<BlackSyncRule> rules = new ArrayList<BlackSyncRule>();
        new Expectations() {{
            blackSyncRuleDao.getAll();
            result = rules;
        }};
        List<BlackSyncRule> syncRules = blackSyncRuleService.getAll();
        Assert.assertEquals(rules.size(), syncRules.size());
    }
    /**
     * 根据订单类型查询
     *
     * @throws Exception
     */
    @Test
    public void testGetByOrderType() throws Exception {
        final List<BlackSyncRule> rules = new ArrayList<BlackSyncRule>();
        new Expectations() {{
            blackSyncRuleDao.getByOrderType(anyInt);
            result = rules;
        }};
        int orderType = 35;
        List<BlackSyncRule> syncRules = blackSyncRuleService.getByOrderType(orderType);
        Assert.assertEquals(rules.size(), syncRules.size());
    }
    /**
     * 删除
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        final BlackSyncRule syncRule = new BlackSyncRule();
        syncRule.setId(21L);
        syncRule.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());
        final int r = 1;
        new Expectations() {{
            blackSyncRuleDao.getById(anyLong);
            result = syncRule;
            blackSyncRuleDao.updateStatus((BlackSyncRule)any);
            result = r;
            jimDBClient.setEx(anyString, anyString, anyInt);
            result = true;
        }};
        Result result = blackSyncRuleService.delete(syncRule.getId(), "uname");
        Assert.assertTrue(result.isSuccess());
    }
}
