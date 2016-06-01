package com.jd.jmi.escort.dao;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 生效规则dao
 * Created by changpan on 2016/1/15.
 */
@Repository
public class RiskEffectRuleDao {

    public static final String NAMESPACE = "com.jd.jmi.escort.dao.RiskEffectRuleMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public RiskEffectRule getById(long id) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectById", id);
    }

    public PaginatedList<RiskEffectRule> list(RiskEffectRule model) {
        PaginatedList<RiskEffectRule> page = new PaginatedArrayList<RiskEffectRule>(model.getPage(),
                model.getPageSize());
        int count = (Integer) sqlSessionTemplate.selectOne(NAMESPACE + "queryCount", model);
        page.setTotalItem(count);
        if (count > 0) {
            model.setStartRow(page.getStartRow() - 1);
            page.addAll((List) sqlSessionTemplate.selectList(NAMESPACE + "queryList", model));
        }
        return page;
    }

    /**
     * 添加
     *
     * @param model
     * @return
     */
    public long add(RiskEffectRule model) {
        int r = sqlSessionTemplate.insert(NAMESPACE + "insert", model);
        return (r == 1 && model.getId() != null) ? model.getId() : 0;
    }

    /**
     * 修改
     *
     * @param model
     * @return
     */
    public int update(RiskEffectRule model) {
        return sqlSessionTemplate.update(NAMESPACE + "updateById", model);
    }

    /**
     * 更新状态
     *
     * @param model
     * @return
     */
    public int updateStatus(RiskEffectRule model) {
        return sqlSessionTemplate.update(NAMESPACE + "updateStatusById", model);
    }

    /**
     * 获取可用的规则
     *
     * @param effectRule
     * @return
     */
    public List<RiskEffectRule> getEnabledByOrderType(RiskEffectRule effectRule) {
        return sqlSessionTemplate.selectList(NAMESPACE + "getEnabledByOrderType", effectRule);
    }

    /**
     * 获取全部规则
     *
     * @param orderType
     * @return
     */
    public List<RiskEffectRule> getByOrderType(int orderType) {
        return sqlSessionTemplate.selectList(NAMESPACE + "getByOrderType", orderType);
    }

    /**
     * 按照判定模型查询个数
     *
     * @param decideId
     * @return
     */
    public int countByDecideId(long decideId) {
        return (Integer) sqlSessionTemplate.selectOne(NAMESPACE + "countByDecideId", decideId);
    }

    /**
     * 根据判定id获取可用的规则
     *
     * @param decideId
     * @return
     */
    public List<RiskEffectRule> getEnableRuleByDecideId(long decideId) {
        return sqlSessionTemplate.selectList(NAMESPACE + "getEnableRuleByDecideId", decideId);
    }

    /**
     * 根据判定id获取可用的规则
     *
     * @param decideId
     * @return
     */
    public List<RiskEffectRule> getRuleByDecideId(long decideId) {
        return sqlSessionTemplate.selectList(NAMESPACE + "getRuleByDecideId", decideId);
    }


}
