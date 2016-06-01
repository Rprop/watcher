package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.AskEnum;
import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.ConditionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实名用户
 * Created by changpan on 2016/2/18.
 */
public class RealNameConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(RealNameConTool.class);

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.REAL_NAME);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,ConditionType.REAL_NAME.getName()+"不合法",value);
        if(StringUtils.isEmpty(value)){
            return result;
        }
        if(AskEnum.getEnumByCode(value) == null){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public String translate(String operate, String value) {
        try{
            if(value.equals("1")){
                return "是" + ConditionType.REAL_NAME.getName();
            }else{
                return "不是" + ConditionType.REAL_NAME.getName();
            }
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
