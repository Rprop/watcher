package com.jd.jmi.escort.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by changpan on 2016/1/4.
 */
public class Result {

    private boolean success;

    private String mes;

    private Map<String, Object> result = new HashMap<String, Object>();

    public Result() {
    }

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String mes) {
        this.success = success;
        this.mes = mes;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMes() {
        return mes;
    }

    public Result setMes(String mes) {
        this.mes = mes;
        return this;
    }

    public Result setSuccessAndMes(boolean success, String mes) {
        this.success = success;
        this.mes = mes;
        return this;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    /**
     * 默认的key
     */
    public static final String DEFAULT_MODEL_KEY = "value";

    /**
     * 当前的key
     */
    private String modelKey = DEFAULT_MODEL_KEY;

    /**
     * 返回码
     */
    private String resultCode;
    private String[] resultCodeParams;


    /**
     * 新增一个返回结果
     *
     * @param obj
     * @return
     */
    public Object addDefaultModel(Object obj) {
        return result.put(DEFAULT_MODEL_KEY, obj);
    }

    /**
     * 新增一个带key的返回结果
     *
     * @param key
     * @param obj
     * @return
     */
    public Object addDefaultModel(String key, Object obj) {
        modelKey = key;
        return result.put(key, obj);
    }

    /**
     * 取出所有的key
     *
     * @return
     */
    public Set<String> keySet() {
        return result.keySet();
    }

    /**
     * 取出整个map对象
     *
     * @return
     */
    public Map<String, Object> getMap() {
        return result;
    }

    /**
     * 取出默认的值
     *
     * @return
     */
    public Object get() {
        return result.get(modelKey);
    }

    /**
     * 取出值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return result.get(key);
    }

    /**
     * 取出值集合
     *
     * @return
     */
    public Collection values() {
        return result.values();
    }

    /**
     * 返回是否成功
     *
     * @return
     */
    public boolean getSuccess() {
        return success;
    }


    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultCode(String resultCode, String... args) {
        this.resultCode = resultCode;
        this.resultCodeParams = args;
    }

    public String[] getResultCodeParams() {
        return resultCodeParams;
    }

    public void setResultCodeParams(String[] resultCodeParams) {
        this.resultCodeParams = resultCodeParams;
    }
}
