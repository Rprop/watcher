package integration.com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskDecideModelService;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskSnapshotService;
import com.jd.jmi.escort.service.RiskUserModelService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"})
public class RiskSnapshotServiceImplTest extends TestCase {


    @Resource
    private RiskDecideModelService riskDecideModelService;

    @Resource
    private RiskUserModelService riskUserModelService;

    @Resource
    private RiskEffectRuleService riskEffectRuleService;

    public void testAdd() throws Exception {

    }

    public void testGetById() throws Exception {

    }

    public void testUpdateRuleIdById() throws Exception {

    }

    public void testGetrRiskEffectRule() throws Exception {

    }

    public void testTranslateById() throws Exception {

    }

    public void testTranslateByIds() throws Exception {

    }

    @Test
    public void initDecideSnap(){
        RiskDecideModel model = new RiskDecideModel();
        model.setPage(1);
        model.setPageSize(100);
        PaginatedList<RiskDecideModel> page =  riskDecideModelService.list(model);
        System.out.println(page.getTotalItem());
        for(RiskDecideModel decideModel : page){
            riskDecideModelService.update(decideModel, JSON.parseArray(decideModel.getRuleData(), ConditionModel.class));
            System.out.println(decideModel.getId());
        }
    }

    @Test
    public void initUserModelSnap(){
        RiskUserModel userModel = new RiskUserModel();
        userModel.setPage(1);
        userModel.setPageSize(100);
        PaginatedList<RiskUserModel> userModels = riskUserModelService.list(userModel);
        System.out.println(userModels.getTotalItem());
        for(RiskUserModel model : userModels){
            riskUserModelService.update(model, JSON.parseArray(model.getRuleData(), ConditionModel.class));
            System.out.println(model.getId());
        }
    }

    @Test
    public void initEffectRuleSnap(){
        RiskEffectRule riskEffectRule = new RiskEffectRule();
        riskEffectRule.setPage(1);
        riskEffectRule.setPageSize(100);
        PaginatedList<RiskEffectRule> effectRules = riskEffectRuleService.list(riskEffectRule);
        System.out.println(effectRules.getTotalItem());
        for(RiskEffectRule model : effectRules){
            riskEffectRuleService.update(model);
            System.out.println(model.getId());
        }
    }

    @Test
    public void initAll(){
        initDecideSnap();
        initEffectRuleSnap();
        initUserModelSnap();
    }




}