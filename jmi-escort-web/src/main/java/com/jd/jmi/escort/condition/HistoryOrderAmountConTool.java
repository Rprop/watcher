package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.model.ConditionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by changpan on 2016/2/18.
 */
public class HistoryOrderAmountConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(HistoryOrderAmountConTool.class);

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.RISK_HISTORY_ORDER_AMOUNT);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,ConditionType.RISK_HISTORY_ORDER_AMOUNT.getName()+"必须为大于0的整数",value);
        try {
            long v = Long.parseLong(result.getValue());
            if(v<0){
                return result;
            }
        }catch (Exception e){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public String translate(String operate, String value) {
        try{
            return ConditionType.RISK_HISTORY_ORDER_AMOUNT.getName()+ getOperateValue(operate)+value;
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
