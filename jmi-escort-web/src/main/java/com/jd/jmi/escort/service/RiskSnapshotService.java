package com.jd.jmi.escort.service;

import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskSnapshot;

import java.util.List;
import java.util.Map;

/**
 * Created by changpan on 2016/2/25.
 */
public interface RiskSnapshotService {

    public long add(RiskSnapshot riskSnapshot);

    public RiskSnapshot getById(long id);

    /**
     * 修改规则id
     *
     * @param id
     * @param ruleId
     * @return
     */
    public int updateRuleIdById(long id, long ruleId);

    /**
     * 通过快照id获取当时的规则
     *
     * @param id
     * @return
     */
    public RiskEffectRule getrRiskEffectRule(long id);

    /**
     * 获取快照的白话文翻译
     *
     * @param id
     * @return
     */
    public RiskSnapshot translateById(long id);

    /**
     * 批量获取快照的白话文翻译
     *
     * @param ids
     * @return
     */
    public Map<Long, RiskSnapshot> translateByIds(long... ids);
}
