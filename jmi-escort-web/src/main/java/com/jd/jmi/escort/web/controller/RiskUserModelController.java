package com.jd.jmi.escort.web.controller;

import com.jd.common.hrm.HrmPrivilege;
import com.jd.jmi.escort.constants.PrivilegeConstant;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.ConditionModels;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.RiskUserModel;
import com.jd.jmi.escort.service.RiskUserModelService;
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
import java.util.List;

/**
 * Created by changpan on 2015/12/30.
 */
@Controller
@RequestMapping("/man/riskUserModel")
public class RiskUserModelController extends  BaseController{

    @Resource
    private RiskUserModelService riskUserModelService;

    private static final Logger logger = LoggerFactory.getLogger(RiskUserModelController.class);

             /**
              * 管理列表
              * @param request
              * @return
              */

    @RequestMapping("/list")
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_LIST)
    public String index(HttpServletRequest request,RiskUserModel model){
        request.setAttribute("queryData",model);
        request.setAttribute("datas",riskUserModelService.list(model));
        return "riskUserModel/list";
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_ADD)
    public String add(){
        return "riskUserModel/add";
    }

    /**
     * 编辑
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping(value = {"/edit/{id}"})
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_UPDATE)
    public ModelAndView edit(ModelAndView mv,@PathVariable(value="id")long id){
        RiskUserModel riskUserModel = riskUserModelService.getById(id);
        mv.setViewName("riskUserModel/edit");
        mv.addObject("riskUser",riskUserModel);
        return mv;
    }


    /**
     * 保存
     * @param riskUserModel 风险用户模型
     * @param conditionModels
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_ADD)
    public Result save(@Validated RiskUserModel riskUserModel,BindingResult bind,ConditionModels conditionModels){
        if(bind.hasErrors()){
            return super.validatedError(bind);
        }
        try{
            riskUserModel.setModifyUser(getUsername());
            List<ConditionModel> conditionist = conditionModels.getConditionModels();
            return  riskUserModelService.save(riskUserModel,conditionist);
        }catch (Exception e){
            logger.error("添加用户模型失败",e);
            return new Result(false,e.getMessage());
        }

    }

    /**
     * 更新
     * @param riskUserModel
     * @param conditionModels
     * @return
     */
    @RequestMapping("/update")
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_UPDATE)
    @ResponseBody
    public Result update(@Validated RiskUserModel riskUserModel,BindingResult bind,ConditionModels conditionModels){
        if(bind.hasErrors()){
            return super.validatedError(bind);
        }
        try{
            riskUserModel.setModifyUser(getUsername());
            List<ConditionModel> conditionist = conditionModels.getConditionModels();
            return  riskUserModelService.update(riskUserModel, conditionist);
        } catch (Exception e){
            logger.error("更新用户模型失败",e);
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
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_UPDATE)
    public Result updateEnabled(@PathVariable(value="id")long id){
        return riskUserModelService.updateEnabled(id,getUsername());
    }

    /**
     * 禁用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateUnabled/{id}"})
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_UPDATE)
    public Result updateUnabled(@PathVariable(value="id")long id){
        return riskUserModelService.updateUnabled(id,getUsername());
    }
    /**
     * 删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"})
    @HrmPrivilege(PrivilegeConstant.USER_MODEL_DEL)
    public Result delete(@PathVariable(value="id")long id){
        return riskUserModelService.delete(id,getUsername());
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
        return riskUserModelService.translate(id);
    }
}
