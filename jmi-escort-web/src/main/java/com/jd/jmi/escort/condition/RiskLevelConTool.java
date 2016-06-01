package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.enums.Operator;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.ConditionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created by changpan on 2016/2/18.
 */
public class RiskLevelConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(RiskLevelConTool.class);

    protected static final Pattern risk_level_pattern = Pattern.compile("^-?[1-9]\\d*(?:\\.\\d{1,2})?|0.\\d{1,2}|0$");

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.RISK_LEVEL);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,"请输入合法的"+ConditionType.RISK_LEVEL.getName()+",例如9.23",value);
        try {
            float v = Float.parseFloat(result.getValue());
            if(v<-10 || v>10){
                return result;
            }
            if(value.charAt(0)=='+'){
                value = value.substring(1);
                result.setValue(value);

            }
//            if(value.charAt(value.length()-1)=='.'){
//                result.setValue(value.substring(0,value.length()-1));
//            }
            if(!risk_level_pattern.matcher(value).matches()){
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
            return ConditionType.RISK_LEVEL.getName()+ getOperateValue(operate)+value;
        }catch (Exception e){
            logger.error("翻译出错operate={},value={}",operate,value);
            return null;
        }
    }

    @Override
    public String translate(ConditionModel conditionModel) {
        return this.translate(conditionModel.getOperator(), conditionModel.getExpectValue());
    }

    public static void main(String[] args) {
        String s = "1";
        System.out.println(risk_level_pattern.matcher(s).matches());
    }
}
