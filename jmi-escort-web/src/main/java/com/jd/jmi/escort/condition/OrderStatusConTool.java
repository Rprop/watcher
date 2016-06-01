package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.enums.JmiOrderStatusEnum;
import com.jd.jmi.escort.common.enums.MemberGradeEnum;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.ConditionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by changpan on 2016/2/18.
 */
public class OrderStatusConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusConTool.class);

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.RISK_ORDER_STATUS);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,ConditionType.RISK_ORDER_STATUS.getName()+"不合法",value);
        if(StringUtils.isEmpty(value)){
            return result;
        }
        if(JmiOrderStatusEnum.getEnumByCode(value) == null){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public String translate(String operate, String value) {
        try{
            return ConditionType.RISK_ORDER_STATUS.getName()+ getOperateValue(operate)+JmiOrderStatusEnum.getEnumByCode(value).getName();
        }catch (Exception e){
            logger.error("翻译出错operate={},value={}",operate,value);
            return null;
        }
    }

    @Override
    public String translate(ConditionModel conditionModel) {
        return this.translate(conditionModel.getOperator(), conditionModel.getExpectValue());
    }
}
