package com.jd.jmi.escort.service.impl;


import com.jd.jmi.escort.pojo.RiskEvent;
import com.jd.jmi.escort.service.BlackSyncRuleService;
import com.jd.jmi.escort.service.RiskEventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"})
public class RiskEventServiceImplTest {

    @Resource
    private RiskEventService riskEventService;

    @Test
    public void testGetRisksByOrderType() {


        try {
            int erp = 20000;

            int Fee = 99000;
            for (int i = 0; i < 1; i++) {

                RiskEvent riskEventVO = new RiskEvent();
                riskEventVO.setUserPin("C-45");
                riskEventVO.setDecideModelId(1713L);
                riskEventVO.setEffectRuleId(1716L);
                riskEventVO.setLevel(4);
                riskEventVO.setOrderType(34);
                riskEventVO.setRiskUserModelId(1732L);
                riskEventVO.setTotalFee(1888L);
                riskEventVO.setUserIp("127.0.0.2");
                riskEventVO.setRuleSnapId(388L);
                riskEventVO.setObjectSnapId(624L);
                riskEventVO.setDecideSnapId(576L);
                riskEventVO.setTriggerDate(new Date());
                riskEventVO.setUuid("xxxxyyyggg12");
                System.out.println("执行保存**************");
                riskEventService.insertRiskEvent(riskEventVO, 1);
            }


        } catch (Exception e) {

            System.out.println(e);
        }


    }
}