package com.jd.jmi.escort.service.impl;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.common.enums.JmiRecordStatusEnum;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import com.jd.jmi.escort.dao.RiskUserModelDao;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.RiskSnapshot;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskSnapshotService;
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
 * @date 2016/3/28
 */
public class RiskUserServiceModelImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RiskUserServiceModelImplTest.class);

    @Tested
    private RiskUserServiceModelImpl riskUserModelService;
    @Injectable
    private RiskUserModelDao riskUserModelDao;
    @Injectable
    private RiskEffectRuleService riskEffectRuleService;
    @Injectable
    private JmiRiskRuleJsfService jmiRiskRuleJsfService;
    @Injectable
    private RiskSnapshotService riskSnapshotService;


    @Test
    public void testSave() throws Exception {
        RiskUserModel riskUserModel = new RiskUserModel();
        List<ConditionModel> conditionModels = new ArrayList<ConditionModel>();

        new Expectations() {{
            riskSnapshotService.add((RiskSnapshot) any);
            result = 1;
            riskUserModelDao.add((RiskUserModel) any);
            result = 1;
            riskSnapshotService.updateRuleIdById(anyLong, anyLong);
            result = null;
        }};

        Result result = riskUserModelService.save(riskUserModel, conditionModels);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testUpdate() throws Exception {
        final RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setId(1L);
        riskUserModel.setOrderType(34);
        riskUserModel.setStatus(1);
        riskUserModel.setModifyUser("bjdiqi");
        List<ConditionModel> conditionModels = new ArrayList<ConditionModel>();

        new Expectations() {{
            riskUserModelDao.getById(anyLong);
            result = riskUserModel;
            riskSnapshotService.add((RiskSnapshot) any);
            result = 1;
            riskUserModelDao.update((RiskUserModel) any);
            result = 1;
            jmiRiskRuleJsfService.deleteRiskCache(riskUserModel.getOrderType());
            result = null;
        }};

        Result result = riskUserModelService.update(riskUserModel, conditionModels);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testUpdateEnabled() throws Exception {
        final RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setId(1L);
        riskUserModel.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());

        new Expectations() {{
            riskUserModelDao.getById(anyLong);
            result = riskUserModel;
            riskUserModelDao.updateStatus((RiskUserModel) any);
            result = 1;
        }};

        Result result = riskUserModelService.updateEnabled(riskUserModel.getId(), "bjdiqi");
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testUpdateUnabled() throws Exception {
        final RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setId(1L);
        riskUserModel.setOrderType(34);
        riskUserModel.setStatus(JmiRecordStatusEnum.VALID.getCode());

        new Expectations() {{
            riskUserModelDao.getById(anyLong);
            result = riskUserModel;
            riskUserModelDao.updateStatus((RiskUserModel) any);
            result = 1;
        }};

        Result result = riskUserModelService.updateUnabled(riskUserModel.getId(), "bjdiqi");
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testGetById() throws Exception {
        final RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setId(1L);
        riskUserModel.setOrderType(34);

        new Expectations() {{
            riskUserModelDao.getById(anyLong);
            result = riskUserModel;
        }};

        RiskUserModel userModel = riskUserModelService.getById(riskUserModel.getId());
        Assert.assertEquals(riskUserModel.getId(), userModel.getId());
    }

    @Test
    public void testGetEnabledByOrderType() throws Exception {
        final List<RiskUserModel> riskUserModels = new ArrayList<RiskUserModel>();
        final RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setId(1L);
        riskUserModel.setModelName("test");
        riskUserModel.setOrderType(34);
        riskUserModels.add(riskUserModel);

        new Expectations() {{
            riskUserModelDao.getEnabledByOrderType(riskUserModel.getOrderType());
            result = riskUserModels;
        }};

        List<RiskUserModel> userModels = riskUserModelService.getEnabledByOrderType(riskUserModel.getOrderType());
        Assert.assertEquals(riskUserModels.size(), userModels.size());
    }

    @Test
    public void testList() throws Exception {
        final PaginatedList<RiskUserModel> userList = new PaginatedArrayList<RiskUserModel>();
        final RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setId(1L);
        riskUserModel.setModelName("test");
        riskUserModel.setOrderType(34);
        userList.add(riskUserModel);

        new Expectations() {{
            riskUserModelDao.list((RiskUserModel) any);
            result = userList;
        }};

        PaginatedList<RiskUserModel> list = riskUserModelService.list(riskUserModel);
        Assert.assertEquals(userList.size(), list.size());
    }

    @Test
    public void testDelete() throws Exception {
        final RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setId(1L);
        riskUserModel.setOrderType(34);
        riskUserModel.setStatus(JmiRecordStatusEnum.IN_VALID.getCode());

        new Expectations() {{
            riskUserModelDao.getById(anyLong);
            result = riskUserModel;
            riskEffectRuleService.hasRuleByUserModel(anyLong, anyInt);
            result = false;
            riskUserModelDao.updateStatus((RiskUserModel) any);
            result = 1;
        }};

        Result result = riskUserModelService.delete(riskUserModel.getId(), "bjdiqi");
        Assert.assertTrue(result.isSuccess());
    }
}
