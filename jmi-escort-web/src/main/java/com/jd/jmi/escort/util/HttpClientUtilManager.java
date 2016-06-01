package com.jd.jmi.escort.util;

import java.util.Map;

/**
 * Created by xuyonggang on 15-3-25.
 */
public interface HttpClientUtilManager {

    public String executeHttpRequestStringByPost(String url, Map<String, String> keyValueMap, int timeout, String charset, boolean retry);
}
