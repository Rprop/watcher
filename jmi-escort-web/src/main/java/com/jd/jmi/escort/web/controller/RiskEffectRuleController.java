package com.jd.jmi.escort.web.controller;

import com.jd.common.hrm.HrmPrivilege;
import com.jd.jmi.escort.constants.PrivilegeConstant;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskDecideModelService;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskUserModelService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 风险生效规则action
 * Created by changpan on 2016/1/15.
 */
@Controller
@RequestMapping("/man/riskEffectRule")
public class RiskEffectRuleController extends BaseController{

    @Resource
    private RiskEffectRuleService riskEffectRuleService;

    @Resource
    private RiskDecideModelService riskDecideModelService;

    @Resource
    private RiskUserModelService riskUserModelService;

    /**
     * 管理列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_LIST)
    public String index(HttpServletRequest request,RiskEffectRule model){
        request.setAttribute("queryData",model);
        request.setAttribute("datas",riskEffectRuleService.list(model));
        return "riskEffectRule/list";
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_ADD)
    public String add(ModelAndView mv){
        return "riskEffectRule/add";
    }

    /**
     * 编辑
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping(value = {"/edit/{id}"})
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_UPDATE)
    public ModelAndView edit(ModelAndView mv,@PathVariable(value="id")long id){
        RiskEffectRule riskEffectRule = riskEffectRuleService.getById(id);
        mv.setViewName("riskEffectRule/edit");
        mv.addObject("data",riskEffectRule);
        return mv;
    }


    /**
     * 保存
     * @param riskEffectRule
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_ADD)
    public Result save(@Validated RiskEffectRule riskEffectRule){
        riskEffectRule.setModifyUser(getUsername());
        return  riskEffectRuleService.save(riskEffectRule);

    }

    /**
     * 更新
     * @param riskEffectRule
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_UPDATE)
    public Result update(RiskEffectRule riskEffectRule){
        riskEffectRule.setModifyUser(getUsername());
        return  riskEffectRuleService.update(riskEffectRule);
    }

    /**
     * 启用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateEnabled/{id}"})
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_UPDATE)
    public Result updateEnabled(@PathVariable(value="id")long id){
        return riskEffectRuleService.updateEnabled(id,getUsername());
    }

    /**
     * 禁用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateUnabled/{id}"})
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_UPDATE)
    public Result updateUnabled(@PathVariable(value="id")long id){
        return riskEffectRuleService.updateUnabled(id,getUsername());
    }
    /**
     * 删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"})
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_DEL)
    public Result delete(@PathVariable(value="id")long id){
        return riskEffectRuleService.delete(id,getUsername());
    }

    /**
     * 获取用户模型
     * @param orderType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getUserModels/{orderType}"})
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_LIST)
    public List<RiskUserModel> getUserModels(@PathVariable(value="orderType")int orderType){
        return riskUserModelService.getEnabledByOrderType(orderType);
    }

    /**
     * 获取用户模型
     * @param orderType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getRiskRiskDecideModels/{orderType}"})
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_LIST)
    public List<RiskDecideModel> getRiskRiskDecideModels(@PathVariable(value="orderType")int orderType){
        return riskDecideModelService.getEnabledByOrderType(orderType);
    }

}
