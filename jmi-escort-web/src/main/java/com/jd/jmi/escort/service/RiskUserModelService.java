package com.jd.jmi.escort.service;


import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.model.ConditionModel;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.RiskUserModel;

import java.util.List;

/**
 * Created by changpan on 2015/12/28.
 */
public interface RiskUserModelService {

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public RiskUserModel getById(long id);

    /**
     * 通过订单类型获取可用的用户模型
     * @param orderType
     * @return
     */
    public List<RiskUserModel> getEnabledByOrderType(int orderType);

    /**
     * 获取所有的风险用户模型
     * @return
     */
    public PaginatedList<RiskUserModel> list(RiskUserModel model);

    /**
     * 保存
     * @param riskUserModel
     * @param conditionModels
     * @return
     */
    public Result save(RiskUserModel riskUserModel, List<ConditionModel> conditionModels);

    /**
     * 更新
     * @param riskUserModel
     * @param conditionModels
     * @return
     */
    public Result update(RiskUserModel riskUserModel,List<ConditionModel> conditionModels);


    /**
     * 启用
     * @param id
     * @param uname
     * @return
     */
    public Result updateEnabled(long id,String uname);

    /**
     * 禁用
     * @param id
     * @param uname
     * @return
     */
    public Result updateUnabled(long id,String uname);

    /**
     * 删除
     * @param id
     * @param uname
     * @return
     */
    public Result delete(long id,String uname);

    public String translate(long id);

    public String translate(RiskUserModel model);



}
