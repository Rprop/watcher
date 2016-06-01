package com.jd.jmi.escort.service;


import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.model.RiskEventModel;
import com.jd.jmi.escort.pojo.RiskEvent;

import java.util.List;

/**
 * 风险事件 Service
 * Created by xuyonggang on 2016/2/16.
 */
public interface RiskEventService {


    /**
     * 查询风险事件
     *
     * @return
     */
    public PaginatedList<RiskEvent> queryRiskEventList(RiskEventModel model);

    /**
     * 保存风险事件
     *
     * @param data
     * @parma source
     *
     * @return
     */
    public void insertRiskEvent(RiskEvent data, Integer source) throws Exception;

    /**
     * 查询风险事件订单合计
     *
     * @return
     */
    public String getRiskEventTotal(RiskEventModel model);


}
