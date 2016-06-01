package com.jd.jmi.escort.util;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.condition.*;
import com.jd.jmi.escort.model.ConditionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by changpan on 2016/2/18.
 */
public class ConditionUtil {

    /**
     * 用户模型工具类
     */
    private static Map<String,ConditionToolInterface> conditionTools = new HashMap<String, ConditionToolInterface>();
    /**
     * 判定模型工具类
     */
    private static Map<String,ConditionToolInterface> decideModelConTools = new HashMap<String, ConditionToolInterface>();

    static {
        conditionTools.put(ConditionType.RISK_LEVEL.getCode(),new RiskLevelConTool());
        conditionTools.put(ConditionType.MEMBER_GRADE.getCode(),new MemberGradeConTool());
        conditionTools.put(ConditionType.NEW_USER.getCode(),new NewUserConTool());
        conditionTools.put(ConditionType.REAL_NAME.getCode(),new RealNameConTool());
        conditionTools.put(ConditionType.IP_LIMIT.getCode(),new IpConTool());

        conditionTools.put(ConditionType.RISK_BEHAVIOR.getCode(),new BehaviorConTool());
        conditionTools.put(ConditionType.RISK_SERVICE_TYPE.getCode(),new ServiceTypeConTool());
        conditionTools.put(ConditionType.RISK_ORDER_STATUS.getCode(),new OrderStatusConTool());
        conditionTools.put(ConditionType.RISK_ORDER_AMOUNT.getCode(),new OrderAmountConTool());
        conditionTools.put(ConditionType.RISK_BRAND_ID.getCode(),new BrandIdConTool());
        conditionTools.put(ConditionType.RISK_SKU_ID.getCode(),new SkuIdConTool());
        conditionTools.put(ConditionType.TAKE_OFF_TIME.getCode(),new TakeOffTimeConTool());
        conditionTools.put(ConditionType.PASSENGER_REAL_NAME.getCode(),new PassengerRealNameConTool());
        conditionTools.put(ConditionType.RISK_HISTORY_ORDER_TOTAL.getCode(),new HistoryOrderTotalConTool());
        conditionTools.put(ConditionType.RISK_HISTORY_ORDER_AMOUNT.getCode(),new HistoryOrderAmountConTool());
    }

    public static ConditionToolInterface getCondition(String code){
        return conditionTools.get(code);
    }

    public static List<String> translate(List<ConditionModel> conditionModels){
        List<String> values = new ArrayList<String>();
        for(ConditionModel conditionModel : conditionModels){
            ConditionToolInterface conditionTool = ConditionUtil.getCondition(conditionModel.getConditionName());
            if(conditionTool!=null){
                String tra = conditionTool.translate(conditionModel);
                if(tra!=null){
                    values.add(tra);
                }
            }
        }
        return values;
    }


}
