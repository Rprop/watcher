package com.jd.jmi.escort.web.controller;

import com.jd.common.hrm.HrmPrivilege;
import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.constants.PrivilegeConstant;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.RiskEventModel;
import com.jd.jmi.escort.model.enums.SnapTypeEnum;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskEvent;
import com.jd.jmi.escort.pojo.RiskSnapshot;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskEventService;
import com.jd.jmi.escort.service.RiskSnapshotService;
import com.jd.jmi.escort.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * 风险事件Controller
 */
@Controller
@RequestMapping("/man/riskEvent")
public class RiskEventController extends BaseController {

    @Resource
    private RiskEventService riskEventService;


    @Resource
    private RiskEffectRuleService riskEffectRuleService;

    @Resource
    private RiskSnapshotService riskSnapshotService;


    /**
     * 风险事件查询
     *
     * @param
     * @return
     */
    @RequestMapping({"/index"})
    @HrmPrivilege(PrivilegeConstant.RISK_EVENT_LIST)
    public String index(@ModelAttribute RiskEventModel riskEventModel, Model model) {

        Result result = new Result(false);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 00);
        now.set(Calendar.MINUTE, 00);
        now.set(Calendar.SECOND, 00);
        riskEventModel.setEventTimeSta(now.getTime());

        riskEventModel.setEventTimeEnd(new Date());

        try {

            PaginatedList<RiskEvent> data = riskEventService.queryRiskEventList(riskEventModel);

            result.addDefaultModel("datas", data);

            result.addDefaultModel("totalFee", riskEventService.getRiskEventTotal(riskEventModel));

            result.addDefaultModel("totalData", data.getTotalItem());

            riskEventModel.setStrEventTimeSta(DateUtils.getDate(riskEventModel.getEventTimeSta(), "yyyy-MM-dd HH:mm:ss"));
            riskEventModel.setStrEventTimeEnd(DateUtils.getDate(riskEventModel.getEventTimeEnd(), "yyyy-MM-dd HH:mm:ss"));
            toVM(model, result);
            result.setSuccess(true);
            if (!result.isSuccess()) {
                return ERROR;
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return "riskEvent/list";
    }


    /**
     * 风险事件查询
     *
     * @param
     * @return
     */
    @RequestMapping({"/list"})
    @HrmPrivilege(PrivilegeConstant.RISK_EVENT_LIST)
    public String select(@ModelAttribute RiskEventModel riskEventModel, Model model) {

        Result result = new Result(false);

        try {
            if (StringUtils.isNotEmpty(riskEventModel.getStrEventTimeSta())) {

                riskEventModel.setEventTimeSta(DateUtils.getDateTime(riskEventModel.getStrEventTimeSta()));

            }

            if (StringUtils.isNotEmpty(riskEventModel.getStrEventTimeEnd())) {

                riskEventModel.setEventTimeEnd(DateUtils.getDateTime(riskEventModel.getStrEventTimeEnd()));

            }

            if (StringUtils.isNotEmpty(riskEventModel.getTotalFeeStaYuan())) {

                Integer s = Integer.valueOf(riskEventModel.getTotalFeeStaYuan());
                s = s * 100;

                riskEventModel.setTotalFeeSta(s.longValue());

            }

            if (StringUtils.isNotEmpty(riskEventModel.getTotalFeeEndYuan())) {

                Integer e = Integer.valueOf(riskEventModel.getTotalFeeEndYuan());
                e = e * 100;

                riskEventModel.setTotalFeeEnd(e.longValue());

            }


            PaginatedList<RiskEvent> data = riskEventService.queryRiskEventList(riskEventModel);


            result.addDefaultModel("totalFee", riskEventService.getRiskEventTotal(riskEventModel));

            result.addDefaultModel("totalData", data.getTotalItem());

            result.addDefaultModel("datas", data);
            result.addDefaultModel("riskEventModel", riskEventModel);

            toVM(model, result);
            result.setSuccess(true);
            if (!result.isSuccess()) {
                return ERROR;
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        // model.a("datas", riskEventService.getRiskEventList(null));
        return "riskEvent/list";
    }

    /**
     * 根据订单类型获取 拦截规则列表
     *
     * @param orderType
     * @return
     */
    @RequestMapping(value = "ajaxRuleList")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.RISK_EVENT_LIST)
    public List<RiskEffectRule> ajaxRuleListByOrderType(Integer orderType) {
        List<RiskEffectRule> list = riskEffectRuleService.getEnabledByOrderType(orderType);

        return list;
    }


    /**
     * 根据订单类型获取 拦截规则列表
     *
     * @param effectDesId
     * @return
     */
    @RequestMapping(value = "ajaxContentsList")
    @ResponseBody
    @HrmPrivilege(PrivilegeConstant.RISK_EVENT_LIST)
    public Map ajaxContentsList(String effectDesId) {
        String[] ids = effectDesId.split(":");
        Map<String, String> allMap = new HashMap<String, String>();
        for (String id : ids) {

            RiskSnapshot riskSnapshot = riskSnapshotService.translateById(new Long(id));

            StringBuffer sb = new StringBuffer();

            if (null != riskSnapshot) {
                int i = 0;
                for (String str : riskSnapshot.getDescribes()) {
                    if (i == 0) {

                        sb.append("<p>" + str + "</p>");
                    } else {
                        sb.append("<p>且 " + str + "</p>");
                    }
                    i++;
                }
                Integer type_ = riskSnapshot.getType();

                String strType = "";

                if (type_ == SnapTypeEnum.DECIDE_MODEK.getCode()) {

                    strType = "modek";
                } else if (type_ == SnapTypeEnum.EFFECT_RULE.getCode()) {
                    strType = "rule";

                } else if (type_ == SnapTypeEnum.USER_MODEL.getCode()) {
                    strType = "user";

                }


                allMap.put(strType, sb.toString());
            }


        }

        return allMap;
    }


}
