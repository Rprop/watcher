package com.jd.jmi.escort.model;

import com.jd.jmi.escort.condition.ConditionResult;
import com.jd.jmi.escort.condition.ConditionToolInterface;
import com.jd.jmi.escort.util.ConditionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changpan on 2016/1/7.
 */
public class ConditionModels {

    /**
     * 限制名称
     */
    private String[] conditionName;

    /**
     * 符号
     */
    private String[] operator;

    /**
     * 期望值
     */
    private String[] expectValue;

    public List<ConditionModel> getConditionModels(){
        List<ConditionModel> list = new ArrayList<ConditionModel>();
        if(conditionName ==null || operator==null || expectValue==null){
            return list;
        }
        if(conditionName.length!=operator.length || conditionName.length!=expectValue.length){
            throw  new RuntimeException("规则参数不匹配");
        }
        for(int i =0 ;i< conditionName.length;i++){
            ConditionToolInterface conditionToolInterface = ConditionUtil.getCondition(conditionName[i]);
            if(conditionToolInterface == null){
                throw  new RuntimeException(conditionName[i]+"规则不存在");
            }
            ConditionResult result = conditionToolInterface.getValidateValue(operator[i],expectValue[i]);
            if(!result.isSuccess()){
                throw  new RuntimeException(result.getMes());
            }
            ConditionModel model = new ConditionModel(conditionName[i],operator[i],result.getValue());
            list.add(model);
        }
        return list;
    }

    public String[] getConditionName() {
        return conditionName;
    }

    public void setConditionName(String[] conditionName) {
        this.conditionName = conditionName;
    }

    public String[] getOperator() {
        return operator;
    }

    public void setOperator(String[] operator) {
        this.operator = operator;
    }

    public String[] getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String[] expectValue) {
        this.expectValue = expectValue;
    }
}
