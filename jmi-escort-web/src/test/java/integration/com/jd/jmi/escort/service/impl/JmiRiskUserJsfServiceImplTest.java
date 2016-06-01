package integration.com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.common.model.risk.RiskRuleVO;
import com.jd.jmi.escort.common.model.risk.RiskUserStatusVO;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import com.jd.jmi.escort.service.impl.JmiRiskUserJsfServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by changpan on 2016/3/10.
 */

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"})
public class JmiRiskUserJsfServiceImplTest {

    @Resource
    private JmiRiskUserJsfServiceImpl jmiRiskUserJsfService;

    @Test
    public void testIsBlackUser(){
        String uname = "test";
        int orderType = 35;
        System.out.println(jmiRiskUserJsfService.isBlackUser(uname,orderType));
    }

    /**
     * 是否是白名单用户
     */
    @Test
    public void testIsWhiteUser(){
        String uname = "test";
        int orderType = 35;
        System.out.println(jmiRiskUserJsfService.isWhiteUser(uname, orderType));
    }

    /**
     * 获取风险用户状态
     */
    @Test
    public void testGetRiskUserStatus(){
        String uname = "test";
        int orderType = 35;
        System.out.println(jmiRiskUserJsfService.getRiskUserStatus(uname, orderType));
    }
}