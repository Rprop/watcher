package com.jd.jmi.escort.condition;

import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.Result;

/**
 * Created by changpan on 2016/2/18.
 */
public interface ConditionToolInterface {

    /**
     *验证value
     * @param value
     * @return
     */
    public ConditionResult getValidateValue(String operate,String value);

    /**
     * 翻译
     * @param operate
     * @param value
     * @return
     */
    public String translate(String operate,String value);

    /**
     * 翻译
     * @param conditionModel
     * @return
     */
    public String translate(ConditionModel conditionModel);


}
