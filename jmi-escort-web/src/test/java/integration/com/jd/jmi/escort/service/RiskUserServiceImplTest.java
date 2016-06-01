package integration.com.jd.jmi.escort.service;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskUserModelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by changpan on 2015/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"}) //加载配置文件
public class RiskUserServiceImplTest {

    @Resource
    private RiskUserModelService riskUserModelService;

    @Test
    public void testGetById(){
        RiskUserModel riskUserBaseModel = riskUserModelService.getById(1L);
        System.out.println(riskUserBaseModel ==null?"空":riskUserBaseModel.getModelName());
    }

    @Test
    public void testTranslate() throws Exception {
        final RiskUserModel riskUserModel = new RiskUserModel();
        ConditionModel model = new ConditionModel();
        model.setConditionName("serviceType");
        model.setExpectValue("83");
        model.setOperator("==");
        List<ConditionModel> conditionModelList = new ArrayList<ConditionModel>();
        conditionModelList.add(model);
        String jsonRule = JSON.toJSONString(conditionModelList);

        riskUserModel.setRuleData(jsonRule);

        String result = riskUserModelService.translate(riskUserModel);
        System.out.println(result);
    }
}
