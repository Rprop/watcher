package com.jd.jmi.escort.web.controller;

import com.jd.common.hrm.HrmPrivilege;
import com.jd.jmi.escort.constants.PrivilegeConstant;
import com.jd.jmi.escort.model.ConditionModels;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.service.RiskDecideModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 风险判定模型controller
 */
@Controller
@RequestMapping("/man/riskDecideModel")
public class RiskDecideModelController extends  BaseController{

    @Resource
    private RiskDecideModelService riskDecideModelService;

    private static final Logger logger = LoggerFactory.getLogger(RiskDecideModelController.class);

            /**
             * 管理列表
             * @param request
             * @return
             */

    @RequestMapping("/list")
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_LIST)
    public String index(HttpServletRequest request,RiskDecideModel model){
        request.setAttribute("queryData",model);
        request.setAttribute("datas",riskDecideModelService.list(model));
        return "riskDecideModel/list";
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_ADD)
    public String add(){
        return "riskDecideModel/add";
    }

    /**
     * 编辑
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping(value = {"/edit/{id}"})
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_UPDATE)
    public ModelAndView edit(ModelAndView mv,@PathVariable(value="id")long id){
        RiskDecideModel riskDecideModel = riskDecideModelService.getById(id);
        mv.setViewName("riskDecideModel/edit");
        mv.addObject("riskDecideModel",riskDecideModel);
        return mv;
    }


    /**
     * 保存
     * @param riskDecideModel 风险用户模型
     * @param conditionModels
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_ADD)
    public Result save(@Validated RiskDecideModel riskDecideModel,BindingResult bind,ConditionModels conditionModels){
        if(bind.hasErrors()){
            return super.validatedError(bind);
        }
        try{
            riskDecideModel.setModifyUser(getUsername());
            return  riskDecideModelService.save(riskDecideModel, conditionModels.getConditionModels());
        } catch (Exception e){
            logger.error("添加判定模型失败",e);
            return new Result(false,e.getMessage());
        }


    }

    /**
     * 更新
     * @param riskDecideModel
     * @param conditionModels
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_UPDATE)
    public Result update(RiskDecideModel riskDecideModel,BindingResult bind,ConditionModels conditionModels){
        if(bind.hasErrors()){
            return super.validatedError(bind);
        }
        try{
            riskDecideModel.setModifyUser(getUsername());
            return  riskDecideModelService.update(riskDecideModel, conditionModels.getConditionModels());
        } catch (Exception e){
            logger.error("更新判定模型失败",e);
            return new Result(false,e.getMessage());
        }

    }

    /**
     * 启用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateEnabled/{id}"})
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_UPDATE)
    public Result updateEnabled(@PathVariable(value="id")long id){
        return riskDecideModelService.updateEnabled(id,getUsername());
    }

    /**
     * 禁用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateUnabled/{id}"})
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_UPDATE)
    public Result updateUnabled(@PathVariable(value="id")long id){
        return riskDecideModelService.updateUnabled(id,getUsername());
    }
    /**
     * 删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"})
    @HrmPrivilege(PrivilegeConstant.DECIDE_MODEL_UPDATE)
    public Result delete(@PathVariable(value="id")long id){
        return riskDecideModelService.delete(id,getUsername());
    }

    /**
     * 翻译
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/translate/{id}"})
    @HrmPrivilege(PrivilegeConstant.EFFECT_RULE_LIST)
    public String translate(@PathVariable(value="id")long id){
        return riskDecideModelService.translate(id);
    }
}
