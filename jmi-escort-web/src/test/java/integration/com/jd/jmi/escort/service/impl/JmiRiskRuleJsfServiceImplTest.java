package integration.com.jd.jmi.escort.service.impl;


import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.common.model.risk.RiskRuleVO;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"})
public class JmiRiskRuleJsfServiceImplTest {

    @Resource
    private JmiRiskRuleJsfService jmiRiskRuleJsfService;

    @Test
    public void testGetRisksByOrderType() {
        List<RiskRuleVO> list = jmiRiskRuleJsfService.getRisksByOrderType(34, 10);
        System.out.println(JSON.toJSONString(list));
    }
}