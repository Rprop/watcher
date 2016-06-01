package com.jd.jmi.escort.util.impl;


import com.jd.jmi.escort.util.HttpClientUtil;
import com.jd.jmi.escort.util.HttpClientUtilManager;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * User: zhangfeng@360buy.com
 * Date: 11-3-6
 * Time: 下午2:50
 */
public class HttpClientUtilManagerImpl implements HttpClientUtilManager {
    private final Logger logger = LoggerFactory.getLogger(HttpClientUtilManagerImpl.class);

    private List<String> notLogParam; //不将此列表中的参数的值打印出来，解决token作为参数被打印出来问题

    public void setNotLogParam(List<String> notLogParam) {
        this.notLogParam = notLogParam;
    }


    public String executeHttpRequestStringByPost(String url, Map<String, String> keyValueMap, int timeout, String charset, boolean retry) {
        String responseBody = "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        try {
            //设置请求参数
            NameValuePair[] parameters = HttpClientUtil.buildPostParameter(keyValueMap, notLogParam);
            if (ArrayUtils.isNotEmpty(parameters)) {
                postMethod.addParameters(parameters);
            }
            logger.info("请求URL：{}", postMethod.getURI());
            HttpClientUtil.setRequestParam(postMethod, timeout, charset, retry);
            int statusCode = client.executeMethod(postMethod);
            logger.info("响应Code：{}", statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                logger.info("request {} failed,the status is not 200,status:{}", url, statusCode);
                return responseBody;
            }
            responseBody = postMethod.getResponseBodyAsString();

            logger.info("响应结果：{}", responseBody);
        } catch (Exception e) {
            logger.error("发生异常！请检查网络和参数，url is " + url, e);
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
            }
            return responseBody;
        }

    }

}
