package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.util.OrderTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by changpan on 2016/2/18.
 */
public class ServiceTypeConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(ServiceTypeConTool.class);

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.RISK_SERVICE_TYPE);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,ConditionType.RISK_SERVICE_TYPE.getName()+"不合法",value);
        if(StringUtils.isEmpty(value)){
            return result;
        }
        if(OrderTypeUtil.getOrderType(StringUtils.parseInt(value)) == null){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public String translate(String operate, String value) {
        try{
            return ConditionType.RISK_SERVICE_TYPE.getName()+ "为"+OrderTypeUtil.getOrderType(StringUtils.parseInt(value));
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
