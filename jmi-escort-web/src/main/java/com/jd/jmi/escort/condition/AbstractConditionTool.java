package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.common.enums.ConditionType;
import com.jd.jmi.escort.common.enums.Operator;
import com.jd.jmi.escort.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by changpan on 2016/2/18.
 */
public abstract class AbstractConditionTool {

    protected static Map<String,String> operatorMap = new HashMap<String, String>();

    protected static final Pattern pattern = Pattern.compile("^([1-9]\\d{0,18}[,])*([1-9]\\d{0,18})$");

    protected static final  String error = "不合法";

    static {
        Operator[] operators = Operator.values();
        for(Operator o : operators){
            operatorMap.put(o.getCode(),o.getName());
        }
    }

    /**
     * 验证操作符
     * @param operator
     * @param conditionType
     * @return
     */
    public ConditionResult validateOperator(String operator,ConditionType conditionType){
        if(StringUtils.isEmpty(operator) || getOperateValue(operator).equals("")){
            return new ConditionResult(false,conditionType.getName()+"操作符不合法","");
        }
        return null;
    }

    /**
     * 获取操作符值
     * @param operator
     * @return
     */
    public static String getOperateValue(String operator){
        if(StringUtils.isEmpty(operator)){
            return "";
        }
        String o = operatorMap.get(operator);
        return o == null? "":o;
    }

}
