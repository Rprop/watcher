package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.enums.MemberGradeEnum;
import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.ConditionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by changpan on 2016/2/18.
 */
public class MemberGradeConTool extends  AbstractConditionTool implements ConditionToolInterface{

    private static final Logger logger = LoggerFactory.getLogger(MemberGradeConTool.class);

    @Override
    public ConditionResult getValidateValue(String operate,String value) {
        ConditionResult result = validateOperator(operate,ConditionType.MEMBER_GRADE);
        if(result !=null){
            return result;
        }
        result = new ConditionResult(false,ConditionType.MEMBER_GRADE.getName()+"不合法",value);
        if(StringUtils.isEmpty(value)){
            return result;
        }
        if(MemberGradeEnum.getEnumByCode(value) == null){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public String translate(String operate, String value) {
        try{
            return ConditionType.MEMBER_GRADE.getName()+ getOperateValue(operate)+MemberGradeEnum.getEnumByCode(value).getName();
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
