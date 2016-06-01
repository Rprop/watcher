package com.jd.jmi.escort.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.jd.common.hrm.HrmPrivilege;
import com.jd.jmi.escort.common.enums.MemberGradeEnum;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.constants.PrivilegeConstant;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.service.BlackUserService;
import com.jd.jmi.escort.service.UserService;
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
 * 风险用户controller
 */
@Controller
@RequestMapping("/man/blackUser")
public class BlackUserController extends BaseController {

    @Resource
    private BlackUserService blackUserService;

    @Resource
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(BlackUserController.class);

    /**
     * 管理列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @HrmPrivilege(PrivilegeConstant.BLACK_USER_LIST)
    public String index(HttpServletRequest request, BlackUserQuery blackUserQuery) {
        request.setAttribute("datas", blackUserService.list(blackUserQuery));
        request.setAttribute("query", blackUserQuery);
        return "blackUser/list";
    }

    /**
     * 保存
     *
     * @param userPin
     * @param orderTypes
     * @return
     */
    @RequestMapping("/saveBlackUser")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.BLACK_USER_ADD)
    public Result saveBlackUser(@RequestParam("userPin") String userPin, @RequestParam("orderType") Integer[] orderTypes) {
        if (StringUtils.isEmpty(userPin)) {
            return new Result(false, "请输入用户名");
        }
        if (orderTypes.length == 0) {
            return new Result(false, "请选择业务类型");
        }
        try {
            return blackUserService.insertBatch(userPin, orderTypes, SourceEnum.BACKGROUND, getUsername());
        }catch (Exception e) {
            logger.error("添加或更新黑名单用户失败", e);
        }
        return new Result(false, "添加或更新黑名单用户失败");
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.BLACK_USER_LIST)
    public Result getUserInfo(@RequestParam("userPin") String userPin) {
        Result result = new Result(false, "获取用户信息失败");
        try {
            logger.info("userPin={}" + userPin);
            JSONObject json = new JSONObject();
            UserBaseInfoVo infoVo = userService.getUserinfo(userPin);
            if (infoVo != null) {
                json.put("infoVo", infoVo);
                json.put("userLevel", MemberGradeEnum.getEnumByCode(infoVo.getUserLevel() + "").getName());
            }
            Double risk = userService.getRisk(userPin);
            if (risk != null) {
                json.put("risk", risk);
            }
            result.setMes(json.toJSONString());
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("获取用户信息失败", e);
        }
        return result;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"})
    @HrmPrivilege(PrivilegeConstant.BLACK_USER_DEL)
    public Result delete(@PathVariable(value = "id") long id) {
        return blackUserService.delete(id, getUsername());
    }


}
