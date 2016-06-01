package com.jd.ump.profiler.common;

import com.jd.ump.profiler.util.CacheUtil;
import com.jd.ump.profiler.util.CustomLogger;

public class BusinessModule {

    public static void businessHandle(String key,
                                      int type, int value, String detail) {
        try {
            key = strPreHandle(key);
            detail = strPreHandle(detail);
            if (detail.length() > 512) {
                detail = detail.substring(0, 512);
            }

            CustomLogger.BusinessLogger.info("{\"time\":" + "\"" + CacheUtil.getNowTime()
                    + "\"" + ",\"key\":" + "\"" + key
                    + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME
                    + "\"" + ",\"type\":" + "\"" + type
                    + "\"" + ",\"value\":" + "\"" + value
                    + "\"" + ",\"detail\":" + "\"" + detail
                    + "\"}");
        } catch (Exception e) {

        }
    }

    public static void businessHandle(String key, int type, int value, String detail, String rtxList, String mailList, String smsList) {
        try {
            key = strPreHandle(key);
            detail = strPreHandle(detail);
            if (detail.length() > 512) {
                detail = detail.substring(0, 512);
            }

            if (null == rtxList) {
                rtxList = "";
            } else {
                rtxList = strPreHandle(rtxList);
            }
            if (null == mailList) {
                mailList = "";
            } else {
                mailList = strPreHandle(mailList);
            }
            if (null == smsList) {
                smsList = "";
            } else {
                smsList = strPreHandle(smsList);
            }

            CustomLogger.BusinessLogger.info("{\"time\":" + "\"" + CacheUtil.getNowTime()
                    + "\"" + ",\"key\":" + "\"" + key
                    + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME
                    + "\"" + ",\"type\":" + "\"" + type
                    + "\"" + ",\"value\":" + "\"" + value
                    + "\"" + ",\"detail\":" + "\"" + detail
                    + "\"" + ",\"RTX\":" + "\"" + rtxList
                    + "\"" + ",\"MAIL\":" + "\"" + mailList
                    + "\"" + ",\"SMS\":" + "\"" + smsList
                    + "\"}");
        } catch (Exception e) {

        }
    }

    private static String strPreHandle(String str) {
        try {
            str = str.replace("\r\n", " ");
            str = str.replace("\r", " ");
            str = str.replace("\n", " ");
            str = str.replace("\\", "\\\\");
            str = str.replace("\"", "\\\"");
            str = str.trim();
        } catch (Exception e) {

        }
        return str;
    }
}