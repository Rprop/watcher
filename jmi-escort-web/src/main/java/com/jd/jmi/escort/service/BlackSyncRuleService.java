package com.jd.jmi.escort.service;

import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.BlackSyncRule;

import java.util.List;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/16
 */
public interface BlackSyncRuleService {

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public BlackSyncRule getById(long id);

    /**
     * 通过订单类型获取可用的黑名单同步规则配置
     *
     * @param orderType
     * @return
     */
    public BlackSyncRule getEnabledByOrderType(int orderType);

    /**
     * 通过订单类型获取黑名单同步规则配置
     *
     * @param orderType
     * @return
     */
    public List<BlackSyncRule> getByOrderType(int orderType);

    /**
     * 获取所有的黑名单同步规则配置
     *
     * @return
     */
    public List<BlackSyncRule> getAll();

    /**
     * 保存
     *
     * @param syncRule
     * @return
     */
    public Result save(BlackSyncRule syncRule);

    /**
     * 更新
     *
     * @param syncRule
     * @return
     */
    public Result update(BlackSyncRule syncRule);


    /**
     * 启用
     *
     * @param id
     * @param uname
     * @return
     */
    public Result updateEnabled(long id, String uname);

    /**
     * 禁用
     *
     * @param id
     * @param uname
     * @return
     */
    public Result updateUnabled(long id, String uname);

    /**
     * 删除
     *
     * @param id
     * @param uname
     * @return
     */
    public Result delete(long id, String uname);


}
