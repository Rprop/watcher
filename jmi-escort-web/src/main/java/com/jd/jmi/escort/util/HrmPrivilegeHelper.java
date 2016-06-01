package com.jd.jmi.escort.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.jmi.cache.JimDBUtils;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.constants.UmpConstants;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * port from com.jd.common.struts.interceptor.HrmPrivilegeHelper. remove
 * dependency with struts, cxf
 * <p/>
 * <p/>
 * 权限认证 User: yangsiyong@360buy.com Date: 2010-6-3 Time: 14:21:20
 * last update by 2014-11-26
 *
 * @author xuetao
 */

public class HrmPrivilegeHelper {
    private final static Logger logger = LoggerFactory.getLogger(HrmPrivilegeHelper.class);

    private JimDBUtils jimDBUtils;
    /**
     * 资源权限缓存时间
     */
    private int cacheTime = 1;

    /**
     * appKey,在调试此demo时请替换成自身的appKey
     */
    private String appKey;
    /**
     * token,在调试此demo时请替换成自身的token
     */
    private String token;
    /**
     * 统一身份API开放中心地址,在调试此demo时请根据接入手册替换URL
     */
    private String uimUrl;
    /**
     * 获取全部用户权限方法 *
     */
    private String method;
    /**
     * 系统代码
     */
    private String systemResCode;

    private int uimTimeout;
    private HttpClientUtilManager httpClientUtilMng;

    /**
     * 取得用户的资源
     *
     * @param pin
     * @return
     */
    public Set<String> getResources(String pin) {
        Set<String> resultSet = null;
        //1.如果当前缓存服务器为空，直接调用接口获取资源。这里面接口有可能异常
        //获取缓存信息,如果为空直接返回
        String key = "erp_admin_hrm_resources_" + pin;
        if (jimDBUtils != null) {
            String resources = jimDBUtils.get(key);
            if (StringUtils.isNotBlank(resources)) {
                //将缓存信息转换为Set对象
                resultSet = JSON.parseObject(resources, Set.class);
                if (resultSet != null) {
                    logger.debug(" resource({}) been the cached hit!", pin);
                    return resultSet;
                }
            }
        }

        //缓存不存在则调用接口信息并且进行缓存
        String str = getAllToken(pin);
        if (StringUtils.isNotEmpty(str)) {
            String[] temp = str.split(",");
            resultSet = new HashSet<String>(Arrays.asList(temp));
            logger.debug("dept resources({}) size:{}", pin, resultSet.size());
        }

        if (resultSet != null && jimDBUtils != null) {
            String rs = JSON.toJSONString(resultSet);
            jimDBUtils.setEx(key, rs, cacheTime);
        }
        return resultSet;
    }

    /**
     * 判断是否有权限
     *
     * @param pin      指定用户。
     * @param resource 被check的权限
     * @return
     */
    public boolean hasHrmPrivilege(String pin, String resource) {
        String[] currentResource = resource.split(",");
        Set<String> resources = getResources(pin);

        if (resources != null && currentResource != null) {
            for (String string : currentResource) {
                if (resources.contains(string)) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * 获取UIM统一权限信息
     *
     * @param userPin
     * @return 菜单列表
     */
    private String getAllToken(String userPin) {
        logger.info("权限I/F erp: " + userPin);
        Map<String, String> params = new HashMap<String, String>();
        //设置appKey
        params.put("app_key", appKey);
        //设置版本号
        params.put("v", "2.0");
        //设置返回数据类型
        params.put("format", "json");
        //设置时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = simpleDateFormat.format(new Date());
        params.put("timestamp", timestamp);
        //设置随机数
        String random = new Random().nextInt(1000000000) + "";
        params.put("random", random);

        params.put("systemResCode", systemResCode);
        params.put("erpId", userPin);
        //生成签名
        String sign = JopApiDigest.getInstance().generate(appKey, token, timestamp, random);
        params.put("sign", sign);//设置参数签名

        //设置API名称
        params.put("method", method);
        CallerInfo info = Profiler.registerInfo(UmpConstants.GET_UIM_AUTH_INTERFACE_ERROR, UmpConstants.ESCORT_WEB_JMI_APP_NAME, false, true);
        StringBuilder sb = new StringBuilder();
        try {
            String str = httpClientUtilMng.executeHttpRequestStringByPost(uimUrl, params, uimTimeout, "UTF-8", true);

            logger.info("权限返回数据 :{} ", str);
            if (StringUtils.isNotBlank(str)) {
                JSONObject jsonObj = JSONArray.parseObject(str);
                // 判断返回结果
                if (null != jsonObj && null != jsonObj.get("menu.get.response")) {
                    UIMMenu uimMenu = com.alibaba.fastjson.JSONObject.parseObject(jsonObj.get("menu.get.response").toString(), UIMMenu.class);
                    if (null != uimMenu && uimMenu.getResStatus().equals("200")) {
                        // 组装返回结果
                        for (Menu menu : uimMenu.getMenus()) {
                            sb.append(menu.getResCodeStr()).append(",");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Profiler.functionError(info);
        } finally {
            Profiler.registerInfoEnd(info);
        }

        logger.info("权限返回数据:{}", sb);
        return sb.toString();
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUimUrl(String uimUrl) {
        this.uimUrl = uimUrl;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUimTimeout(int uimTimeout) {
        this.uimTimeout = uimTimeout;
    }

    public void setSystemResCode(String systemResCode) {
        this.systemResCode = systemResCode;
    }

    public void setHttpClientUtilMng(HttpClientUtilManager httpClientUtilMng) {
        this.httpClientUtilMng = httpClientUtilMng;
    }

    public void setJimDBUtils(JimDBUtils jimDBUtils) {
        this.jimDBUtils = jimDBUtils;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }
}