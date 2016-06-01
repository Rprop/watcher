package com.jd.jmi.escort.constants;

/**
 * Created by changpan on 2016/1/28.
 */
public class UmpConstants {
    /**
     * 京米平台 虚拟镖局web应用名称
     */
    public static final String ESCORT_WEB_JMI_APP_NAME = "escort-web";

    /**
     * 根据订单类型获取风险规则
     */
    public static final String GET_RISKS_BY_ORDERTYPE = "escort_web_jmi_jsf_getRisksByOrderType";

    /**
     * 风险事件同步mq
     */
    public static final String GET_RISKEVENT_JMQ = "escort_web_jmi_jmq_riskevent";

    public static final String WEB_QUERY_RISK_LEVEL = "web_query_risk_level";//风险等级
    public static final String WEB_QUERY_MEMBER_GRADE = "web_query_member_grade";//用户等级
    public static final String WEB_QUERY_NEW_USER = "web_query_new_user";//是否虚拟新用户
    public static final String WEB_QUERY_REAL_NAME = "web_query_real_name";//实名信息

    /**
     * 调用UIM权限接口 获取用户访问权限失败
     */
    public static final String GET_UIM_AUTH_INTERFACE_ERROR = "jmi.get.uim.auth.app.menus.error";

}
