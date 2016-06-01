package com.jd.jmi.escort.web.intercept;


//import com.jd.games.common.util.StringUtils;
//import com.jd.jmi.web.util.HrmPrivilegeHelper;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.util.HrmPrivilegeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拦截方法执行时的注释。以确定是否有权限执行。 <br/>
 * 做的Logincontext认证<br/>
 * User: yangsiyong@360buy.com<br/>
 * Date: 2010-6-2<br/>
 * Time: 11:09:54<br/>
 * <p/>
 * port from com.jd.common.struts.interceptor.HrmDotnetPrivilegeInterceptor,
 * remove dependency with struts.
 * <p/>
 * last update by 2014-11-26
 *
 * @author xuetao
 */
public class HrmPrivilegeInterceptor extends HandlerInterceptorAdapter {
    private boolean closeHrmPrivilege = false;
    private Logger logger = LoggerFactory.getLogger(HrmPrivilegeInterceptor.class);

    private HrmPrivilegeHelper hrmPrivilegeHelper;

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (closeHrmPrivilege) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
//        //获取请求前面的注解
//        HrmPrivilege annotation = ((HandlerMethod) handler).getMethodAnnotation(HrmPrivilege.class);
//
//        //为空拦截器继续走
//        if (annotation == null) {
//            return true;
//        }
//
//        //获取注解里面的值
//        String code = annotation.value();// 资源需要的权限
//        if (StringUtils.isNotEmpty(code)) {
//            String username = getUsername();
//
//            //判断当前用户的权限和注解里面的值 是否匹配。不匹配则抛出自定义权限异常
//            if (username == null || !hrmPrivilegeHelper.hasHrmPrivilege(username, code)) {
//                /** ajax 请求时, 验证权限失败 提示错误信息  begin */
//                boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
//                String ajaxFlag = null == request.getParameter("ajax") ? "false" : request.getParameter("ajax");
//                boolean isAjax = ajax || ajaxFlag.equalsIgnoreCase("true");
//                if (isAjax) {
//                    try {
//                        response.setContentType("text/html; charset=utf-8");
//                        PrintWriter writer = response.getWriter();
//                        Result result = new Result(false, "非常抱歉，您没有此页面权限！");
//                        writer.write(JSON.toJSONString(result));
//                        writer.flush();
//                    } catch (IOException e) {
//                        PrintWriter writer = response.getWriter();
//                        writer.write("系统异常！");
//                        writer.flush();
//                    } finally {
//                        return false;
//                    }
//                    /** ajax 请求时, 验证权限失败 提示错误信息  end */
//                } else {
//                    throw new IllegalHrmPrivilegeException("非常抱歉，您没有此页面权限！", code.split(","));
//                }
//            }
//            logger.info("用户({}) 正在操作：{}", username, request.getRequestURI());
//        }

        return true;
    }

    public void setCloseHrmPrivilege(boolean closeHrmPrivilege) {
        this.closeHrmPrivilege = closeHrmPrivilege;
    }

    public void setHrmPrivilegeHelper(HrmPrivilegeHelper hrmPrivilegeHelper) {
        this.hrmPrivilegeHelper = hrmPrivilegeHelper;
    }
}