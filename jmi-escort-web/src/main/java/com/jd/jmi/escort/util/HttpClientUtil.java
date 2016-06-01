package com.jd.jmi.escort.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xuyonggang
 * Date: 15-03-26
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientUtil {
    private static final Logger log = LogManager.getLogger(HttpClientUtil.class);

    public static NameValuePair[] buildPostParameter(Map<String, String> keyValueMap, List<String> notLogParam) {
        if (keyValueMap != null) {
            Iterator it = keyValueMap.entrySet().iterator();
            NameValuePair[] parameters = new NameValuePair[keyValueMap.size()];
            int c = 0;
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                logParam(entry, "executeHttpRequestString", notLogParam);
                NameValuePair nvp = new NameValuePair();
                nvp.setName(entry.getKey());
                nvp.setValue(entry.getValue());
                parameters[c] = nvp;
                c++;
            }
            return parameters;
        }
        return null;
    }

    public static String buildPostParameter(String url, Map<String, String> keyValueMap, List<String> notLogParam) {
        if (MapUtils.isNotEmpty(keyValueMap)) {
            //设置请求参数
            Iterator it = keyValueMap.entrySet().iterator();
            StringBuffer sb = new StringBuffer(url).append("?");
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } else {
            return url;
        }
    }

    /**
     * @param httpMethod
     * @param timeout    超时时间
     * @param charset    编码
     * @param retry      是否需要重试
     * @return
     */
    public static HttpMethod setRequestParam(HttpMethod httpMethod, int timeout, String charset, boolean retry) {
        HttpMethodRetryHandler httpMethodRetryHandler;
        if (retry) {
            httpMethodRetryHandler = new DefaultHttpMethodRetryHandler();
        } else {
            httpMethodRetryHandler = new DefaultHttpMethodRetryHandler(0, false);

        }
        httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, httpMethodRetryHandler);
        httpMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        httpMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
        return httpMethod;
    }

    /**
     * 打印http请求的参数,如果参数名称在notLogParam列表中，则不打印日志
     *
     * @param entry      包含参数的名称和值
     * @param methodName 方法的名称
     */
    private static void logParam(Map.Entry<String, String> entry, String methodName, List<String> notLogParam) {
        if (CollectionUtils.isEmpty(notLogParam) || !notLogParam.contains(entry.getKey())) {
            log.info("Method:" + methodName + "---->keyValueMap=======" + entry.getKey() + "======" + entry.getValue());
        }
    }
}
