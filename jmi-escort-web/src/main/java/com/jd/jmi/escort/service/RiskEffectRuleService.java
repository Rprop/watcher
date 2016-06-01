package com.jd.jmi.escort.service;

import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.RiskEffectRule;

import java.util.List;

/**
 *
 * Created by changpan on 2016/1/15.
 */
public interface RiskEffectRuleService {

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public RiskEffectRule getById(long id);

    /**
     * 通过订单类型获取可用的生效规则
     * @param orderType
     * @return
     */
    public List<RiskEffectRule> getEnabledByOrderType(int orderType);
    /**
     * 通过订单类型获取可用的生效规则
     * @param orderType
     * @return
     */
    public List<RiskEffectRule> getEnabledByOrderType(int orderType,Integer processType);

    /**
     * 获取所有的风险生效规则
     * @return
     */
    public PaginatedList<RiskEffectRule> list(RiskEffectRule model);

    /**
     * 保存
     * @param model
     * @return
     */
    public Result save(RiskEffectRule model);

    /**
     * 更新
     * @param model
     * @return
     */
    public Result update(RiskEffectRule model);


    /**
     * 启用
     * @param id
     * @param uname
     * @return
     */
    public Result updateEnabled(long id, String uname);

    /**
     * 禁用
     * @param id
     * @param uname
     * @return
     */
    public Result updateUnabled(long id, String uname);

    /**
     * 删除
     * @param id
     * @param uname
     * @return
     */
    public Result delete(long id, String uname);

    /**
     * 是否含有该判定模型的规则
     * @param decideId
     * @return
     */
    public boolean hasRuleByDecide(long decideId);

    /**
     * 是否含有该用户模型的规则
     * @param userModelId
     * @param  orderType
     * @return
     */
    public boolean hasRuleByUserModel(long userModelId,int orderType);


}
