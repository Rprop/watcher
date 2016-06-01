package com.jd.jmi.escort.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.jd.common.hrm.HrmPrivilege;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.constants.PrivilegeConstant;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.service.BlackUserService;
import com.jd.jmi.escort.service.UserService;
import com.jd.jmi.escort.service.WhiteUserService;
import com.jd.user.sdk.export.domain.UserBaseInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 风险判定模型controller
 */
@Controller
@RequestMapping("/man/whiteUser")
public class WhiteUserController extends BaseController {

    @Resource
    private WhiteUserService whiteUserService;
    private static final Logger logger = LoggerFactory.getLogger(WhiteUserController.class);

    /**
     * 管理列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @HrmPrivilege(PrivilegeConstant.WHITE_USER_LIST)
    public String index(HttpServletRequest request, BlackUserQuery blackUserQuery) {
        request.setAttribute("datas", whiteUserService.list(blackUserQuery));
        request.setAttribute("query", blackUserQuery);
        return "whiteUser/list";
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"})
    @HrmPrivilege(PrivilegeConstant.WHITE_USER_DEL)
    public Result delete(@PathVariable(value = "id") long id) {
        return whiteUserService.delete(id, "uname");
    }


    @ResponseBody
    @RequestMapping(value = {"/add"})
    @HrmPrivilege(PrivilegeConstant.WHITE_USER_ADD)
    public Result add(@RequestParam("userPin") String userPin, @RequestParam("orderType") Integer[] orderTypes) {
        if (StringUtils.isEmpty(userPin)) {
            return new Result(false, "请输入用户名");
        }
        if (orderTypes.length == 0) {
            return new Result(false, "请选择订单类型");
        }
        return whiteUserService.insertBatch(userPin, orderTypes, SourceEnum.BACKGROUND, getUsername());
    }
}
