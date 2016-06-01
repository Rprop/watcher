package com.jd.jmi.escort.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/5
 * Version:1.0
 */
@Controller
@RequestMapping("/man/processType")
public class ProcessTypeController {
    /**
     * 获取用户模型
     *
     * @param orderType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/ajaxProcessType/{orderType}"})
    public Object getRiskRiskDecideModels(@PathVariable(value = "orderType") int orderType) {
        JSONArray array = new JSONArray();
        JSONObject order = new JSONObject();
        order.put("id", 1);
        order.put("name", "提交订单验证");
        array.add(order);

        JSONObject input = new JSONObject();
        input.put("id", 10);
        input.put("name", "使用资产流程");
        array.add(input);

        return array;
    }
}
