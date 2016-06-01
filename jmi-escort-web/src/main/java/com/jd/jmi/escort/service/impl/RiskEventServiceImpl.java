package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.common.enums.BlackUserLevelEnum;
import com.jd.jmi.escort.common.enums.RiskEventSourceEnum;
import com.jd.jmi.escort.common.exceptions.ParameterException;
import com.jd.jmi.escort.dao.RiskEventDao;
import com.jd.jmi.escort.model.RiskEventModel;
import com.jd.jmi.escort.pojo.BlackSyncRule;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskEvent;
import com.jd.jmi.escort.service.BlackUserService;
import com.jd.jmi.escort.service.RiskEffectRuleService;
import com.jd.jmi.escort.service.RiskEventService;
import com.jd.jmi.escort.util.DateUtils;
import com.jd.jmi.escort.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * 风险事件处理实现Service
 * Created by xuyonggang on 2016/2/16.
 */
@Service("riskEventService")
public class RiskEventServiceImpl implements RiskEventService {

    private static final Logger logger = LoggerFactory.getLogger(RiskEventServiceImpl.class);

    @Resource
    private RiskEventDao riskEventDao;

    @Resource
    private BlackSyncRuleServiceImpl blackSyncRuleService;

    @Resource
    private BlackUserService blackUserService;

    @Resource
    private RiskEffectRuleService riskEffectRuleService;

    /**
     * 查询风险事件订单合计
     *
     * @return
     */
    public String getRiskEventTotal(RiskEventModel model) {

        return riskEventDao.countRiskEventTotal(model);

    }


    /**
     * 获取风险事件 list
     *
     * @param model
     * @return
     */
    @Override
    public PaginatedList<RiskEvent> queryRiskEventList(RiskEventModel model) {

        logger.info("获取风险事件 参数:" + JSON.toJSONString(model));

        PaginatedList<RiskEvent> page = new PaginatedArrayList<RiskEvent>(model.getPage(), model.getPageSize());

        try {

            int count = riskEventDao.countJmiPayokBury(model);
            page.setTotalItem(count);

            logger.info("获取风险事件 总件数:" + count);


            if (count > 0) {
                model.setStartRow(page.getStartRow() - 1);


                List<RiskEvent> list = riskEventDao.getRiskEventList(model);

                logger.info("获取风险事件 结果长度:" + list.size());
                // 风险来源名称获取
                for (RiskEvent da : list) {
                    da.setSourceName(RiskEventSourceEnum.getName(da.getSource()).getName());
                }
                page.addAll(list);

            }
        } catch (Exception e) {
            logger.error("获取风险事件失败 参数:" + JSON.toJSONString(model), e);
        }
        return page;

    }


    /**
     * 保存风险事件
     *
     * @param risk   事件数据
     * @param source 风控来源
     */
    @Override
    public void insertRiskEvent(RiskEvent risk, Integer source) throws Exception {

        if (null != risk && null == risk.getEffectRuleId()) {

            throw new ParameterException("风控生效规则id不能为空!");

        }
        //获取规则
        RiskEffectRule effectRule = riskEffectRuleService.getById(risk.getEffectRuleId());
        // 规则名称
        risk.setEffectRuleName(effectRule.getName());

        // 验证数据
        ValidateUtil.validate(risk);

        StringBuffer effectDesId = new StringBuffer();
        // 拼装 快照组合id
        effectDesId.append(risk.getObjectSnapId() + ":" + risk.getRuleSnapId() + ":" + risk.getDecideSnapId());

        risk.setEffectDesId(effectDesId.toString());

        risk.setSource(source);

        risk.setIsDelUser(0);

        logger.info("风险用户 保存数据为 ： " + JSON.toJSONString(risk));
        // save data
        riskEventDao.insertRiskEvent(risk);



        // 大于0级事件处理
        if (0 < risk.getLevel()) {
            BlackSyncRule rule = blackSyncRuleService.getEnabledByOrderType(risk.getOrderType());
            RiskEventModel model = new RiskEventModel();

            model.setEventTimeEnd(DateUtils.getDateTime());

            Calendar beginTime = Calendar.getInstance();
            beginTime.add(Calendar.DAY_OF_MONTH, -rule.getTimeInterval());

            model.setEventTimeSta(beginTime.getTime());
            model.setUserPin(risk.getUserPin());
            model.setOrderType(risk.getOrderType());
            model.setIsDelUser(0);
            // TODO 固定值
            model.setLevel(1);
            logger.info("风险用户 查询参数 ： " + JSON.toJSONString(model));
            int count = riskEventDao.countJmiPayokBury(model);
            logger.info("风险用户 统计次数 ： " + count);

            if (count >= rule.getNum()) {
                // save 黑名单用户
                blackUserService.merge(risk.getUserPin(), risk.getOrderType(), BlackUserLevelEnum.BLACKUSER.getCode());

                risk.setBlackUserLevel(BlackUserLevelEnum.BLACKUSER.getCode());
            } else {
                // save 风险用户
                blackUserService.merge(risk.getUserPin(), risk.getOrderType(), BlackUserLevelEnum.RISKUSER.getCode());
                risk.setBlackUserLevel(BlackUserLevelEnum.RISKUSER.getCode());
            }

            riskEventDao.updateForBlackUserLevel(risk.getId(), risk.getBlackUserLevel());


        }

    }


}
