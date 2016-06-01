package com.jd.jmi.escort.web.controller;

import com.alibaba.fastjson.JSON;
import com.jd.common.hrm.HrmPrivilege;
import com.jd.jmi.escort.constants.PrivilegeConstant;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.BlackSyncRule;
import com.jd.jmi.escort.service.BlackSyncRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/16
 */
@Controller
@RequestMapping("/man/blackSyncRule")
public class BlackSyncRuleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BlackSyncRuleController.class);
    @Resource
    private BlackSyncRuleService blackSyncRuleService;

    /**
     * 管理列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_LIST)
    public String index(HttpServletRequest request) {
        request.setAttribute("datas", blackSyncRuleService.getAll());
        return "blackSyncRule/list";
    }

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping("/add")
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_ADD)
    public String add() {
        return "blackSyncRule/add";
    }

    /**
     * 编辑
     *
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping(value = {"/edit/{id}"})
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_UPDATE)
    public ModelAndView edit(ModelAndView mv, @PathVariable(value = "id") long id) {
        BlackSyncRule syncRule = blackSyncRuleService.getById(id);
        mv.setViewName("blackSyncRule/edit");
        mv.addObject("syncRule", syncRule);
        return mv;
    }

    /**
     * 保存
     *
     * @param syncRule 黑名单同步规则
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_ADD)
    public Result save(@Validated BlackSyncRule syncRule) {
        Result result = new Result(false);
        try {
            logger.info("syncRule={}" + JSON.toJSONString(syncRule));
            Result validateResult = validate(syncRule);
            if (validateResult.isSuccess()) {
                List<BlackSyncRule> syncRuleList = blackSyncRuleService.getByOrderType(syncRule.getOrderType());
                if (null != syncRuleList && syncRuleList.size() > 0) {
                    result.setMes("存在相同业务类型的规则，请核对再添加！");
                    return result;
                } else {
                    syncRule.setModifyUser(getUsername());
                    return blackSyncRuleService.save(syncRule);
                }
            } else {
                return validateResult;
            }
        } catch (Exception e) {
            String mes = "保存黑名单同步规则失败";
            result.setMes(mes);
            logger.error(mes, e);
        }
        return result;
    }

    /**
     * 更新
     *
     * @param syncRule
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_UPDATE)
    public Result update(BlackSyncRule syncRule) {
        Result result = new Result(false);
        try {
            logger.info("syncRule={}" + JSON.toJSONString(syncRule));
            Result validateResult = validate(syncRule);
            if (validateResult.isSuccess()) {
                syncRule.setModifyUser(getUsername());
                return blackSyncRuleService.update(syncRule);
            } else {
                return validateResult;
            }
        } catch (Exception e) {
            String mes = "更新黑名单同步规则失败";
            result.setMes(mes);
            logger.error(mes, e);
        }
        return result;

    }

    /**
     * 启用
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateEnabled/{id}"})
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_UPDATE)
    public Result updateEnabled(@PathVariable(value = "id") long id) {
        return blackSyncRuleService.updateEnabled(id, getUsername());
    }

    /**
     * 禁用
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateUnabled/{id}"})
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_UPDATE)
    public Result updateUnabled(@PathVariable(value = "id") long id) {
        return blackSyncRuleService.updateUnabled(id, getUsername());
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"})
    @HrmPrivilege(PrivilegeConstant.BLACK_SYNC_RULE_DEL)
    public Result delete(@PathVariable(value = "id") long id) {
        return blackSyncRuleService.delete(id, getUsername());
    }

    /**
     * 校验参数
     *
     * @param syncRule
     * @return
     */
    private Result validate(BlackSyncRule syncRule) {
        Result result = new Result(true);
        int maxValue = 200;

        if (syncRule.getNum().intValue() > maxValue) {
            result.setSuccess(false);
            result.setMes("触发规则次数设置不能大于" + maxValue);
        }
        return result;
    }

}
