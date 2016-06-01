package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.ConditionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实名用户
 * Created by changpan on 2016/2/18.
 */
public class IpConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(IpConTool.class);

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.IP_LIMIT);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,ConditionType.IP_LIMIT.getName()+"不合法",value);
        if(StringUtils.isEmpty(value) || !value.equals("1")){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public String translate(String operate, String value) {
        try{
            if(value.equals("1")){
                return "是" + ConditionType.IP_LIMIT.getName();
            }else{
                return "不是" + ConditionType.IP_LIMIT.getName();
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
