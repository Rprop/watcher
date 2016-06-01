package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.pojo.RiskUserModel;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class RiskUserServiceImplTest extends TestCase {

    public void testGetById() throws Exception {
        RiskUserModel riskUserModel = new RiskUserModel();
        riskUserModel.setModelName("测试");
        riskUserModel.setOrderType(35);
        List<ConditionModel> conditionModels = new ArrayList<ConditionModel>();
        ConditionModel conditionModel = new ConditionModel();
        conditionModel.setConditionName("user");
        conditionModel.setExpectValue("1");
        conditionModel.setOperator("==");
        conditionModels.add(conditionModel);
        riskUserModel.setRuleData(JSON.toJSONString(conditionModels));
        System.out.println(JSON.toJSON(riskUserModel));
    }

    public void testGetEnabledByOrderType() throws Exception {

    }

    public void testGetAll() throws Exception {

    }
}