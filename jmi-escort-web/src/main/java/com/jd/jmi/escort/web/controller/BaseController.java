package com.jd.jmi.escort.web.controller;

import com.jd.common.util.StringUtils;
import com.jd.common.web.DotnetAuthenticationTicket;
import com.jd.common.web.LoginContext;
import com.jd.common.web.cookie.CookieUtils;
import com.jd.jmi.escort.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Created by changpan on 2016/2/16.
 */
public class BaseController {

    /**
     * 表单验证
     * @param bind
     */
    public final Result validatedError(BindingResult bind){
        if(bind.hasErrors()){
            Result result = new Result();
            FieldError error = bind.getFieldErrors().get(0);
            result.setMes(error.getField() + error.getDefaultMessage());
            result.setSuccess(false);
        }
        return null;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
    /**
     * 错误页面
     */
    protected static final String ERROR = "errors/error";
    /**
     * 返回的错误信息
     */
    protected static final String ACTION_MESSAGE = "actionMessage";
    /**
     * 返回带参数的错误信息
     */
    protected static final String ACTION_MESSAGE_PARAMS = "actionMessageParams";
    /**
     * 获取页码的字段名
     */
    protected static final String PAGE_PARAM = "page";
    /**
     * 默认每页20条
     */
    protected static final int PAGE_SIZE = 20;


    @ModelAttribute(value = PAGE_PARAM)
    public int setRequestAndResponse(HttpServletRequest request) {
        return getPage(request);
    }

    /**
     * 获取浏览器cookie
     *
     * @param name
     * @return
     */
    public String getCookieValue(HttpServletRequest request, String name) {
        return null;//cookieUtils.getCookieValue(request, name);
    }


    /**
     * 获取页码,大于100 则初始化为1
     *
     * @return
     */
    private int getPage(HttpServletRequest request) {

        //默认第一页
        int p = 1;

        String page = request.getParameter(PAGE_PARAM);
        if (StringUtils.isNotBlank(page)) {
            try {
                int pageTemp = Integer.parseInt(page);
                if (pageTemp <= 100) {
                    return pageTemp;
                }
            } catch (Exception e) {
                LOGGER.error("Page translation exception！");
            }
        } else {
            return p;
        }
        return p;
    }

    /**
     * 错误信息和返回值封装
     *
     * @param model
     * @param result
     */
    public final void toVM(Model model, Result result) {
        if (result != null && model != null) {
            Set<String> set = result.keySet();
            for (String key : set) {
                model.addAttribute(key, result.get(key));
            }
            String resultCode = result.getResultCode();
            if (StringUtils.isNotBlank(resultCode)) {
                String[] params = result.getResultCodeParams();
                model.addAttribute(ACTION_MESSAGE, resultCode);
                if (params != null && params.length > 0) {
                    model.addAttribute(ACTION_MESSAGE_PARAMS, params);
                }
            }
        }
    }

    /**
     * 错误信息和返回值封装
     *
     * @param modelAndView
     * @param result
     */
    public final void toVM(ModelAndView modelAndView, Result result) {
        if (result != null && modelAndView != null) {
            Set<String> set = result.keySet();
            for (String key : set) {
                modelAndView.addObject(key, result.get(key));
            }
            String resultCode = result.getResultCode();
            if (StringUtils.isNotBlank(resultCode)) {
                String[] params = result.getResultCodeParams();
                modelAndView.addObject(ACTION_MESSAGE, resultCode);
                if (params != null && params.length > 0) {
                    modelAndView.addObject(ACTION_MESSAGE_PARAMS, params);
                }
            }
            if (!result.isSuccess()) {
                modelAndView.setViewName(ERROR);
            }
        }
    }

    /**
     * 表单验证失败
     * @param bind
     * @param modelAndView
     * @return
     */
    public final  ModelAndView validatedError(BindingResult bind,ModelAndView modelAndView){
        if(bind.hasErrors()){
            FieldError error = bind.getFieldErrors().get(0);
            modelAndView.addObject(ACTION_MESSAGE, error.getField() + error.getDefaultMessage());
            modelAndView.setViewName(ERROR);
        }
        return modelAndView;
    }
//
//    /**
//     * 写cookie
//     *
//     * @param response
//     * @param name
//     * @param value
//     */
//    public void setCookie(HttpServletResponse response, String name, String value) {
//        cookieUtils.setCookie(response, name, value);
//    }
//
//
//    /**
//     * 删除cookie
//     *
//     * @param response
//     * @param name
//     */
//    public void deleteCookie(HttpServletResponse response, String name) {
//        cookieUtils.deleteCookie(response, name);
//    }
//
//    /**
//     * 取得用户名
//     *
//     * @return
//     */
//    protected String getUsername() {
//        String username = getDotnetTicketUsername();
//        if (username == null) {
//            LoginContext context = LoginContext.getLoginContext();// 只取hrm相关的信息
//            if (context != null && StringUtils.isNotBlank(context.getPin())) {
//                username = context.getPin();
//            }
//        }
//        return username;
//    }
//
//    protected String getDotnetTicketUsername() {
//        String username = null;
//        DotnetAuthenticationTicket ticket = DotnetAuthenticationTicket.getTicket();
//        if (ticket != null && StringUtils.isNotBlank(ticket.getUsername())) {
//            username = ticket.getUsername();
//        }
//        return username;
//    }
}
