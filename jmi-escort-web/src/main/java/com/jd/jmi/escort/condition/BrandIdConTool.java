package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.enums.JmiOrderStatusEnum;
import com.jd.jmi.escort.common.enums.TakeOffTimeEnum;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.ConditionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * 实名用户
 * Created by changpan on 2016/2/18.
 */
public class BrandIdConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(BrandIdConTool.class);

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.RISK_BRAND_ID);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,"请输入合法的"+ConditionType.RISK_BRAND_ID.getName()+"，多个以逗号分隔",value);
        if(StringUtils.isEmpty(value)){
            return result;
        }
        if(!pattern.matcher(value).matches()){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public String translate(String operate, String value) {
        try{
            return ConditionType.RISK_BRAND_ID.getName()+ "在"+ value+"之中";
        }catch (Exception e){
            logger.error("翻译出错operate={},value={}",operate,value);
            return null;
        }
    }

    @Override
    public String translate(ConditionModel conditionModel) {
        return this.translate(conditionModel.getOperator(),conditionModel.getExpectValue());
    }

}
