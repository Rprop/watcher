package com.jd.jmi.escort.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by changpan on 2015/12/30.
 */
@Controller
public class HomeController extends BaseController {

    @Resource
    private String[] logoutCookie;

    /**
     * 设置当前用户的地区
     *
     * @return
     */
    @RequestMapping(value = {"/", "index"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }

    //退出
    @RequestMapping(value = {"logout"}, method = RequestMethod.GET)
    public String logout(HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        //删除上下文信息
//        if (LoginContext.getLoginContext() != null) {
//            LoginContext.remove();
//        }
        //删除cookie,指定删除的cookie
        if (logoutCookie != null) {
            for (String s : logoutCookie) {
                deleteCookie(response, s);
            }
        }
        return "redirect:/";
    }

    @RequestMapping(value = {"404"}, method = RequestMethod.GET)
    public String error404() {
        return "errors/404";
    }


    @RequestMapping(value = {"500", "405"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String error405() {
        return "errors/error";
    }

    @RequestMapping(value = {"404"})
    public String access(){
        return "com.jd.common.hrm.IllegalHrmPrivilegeException";
    }
}
