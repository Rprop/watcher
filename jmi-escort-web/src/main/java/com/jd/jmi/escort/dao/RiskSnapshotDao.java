package com.jd.jmi.escort.dao;

import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.pojo.RiskSnapshot;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by changpan on 2016/2/25.
 */
@Repository
public class RiskSnapshotDao {

    public static final String NAMESPACE = "com.jd.jmi.escort.dao.RiskSnapshotMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public RiskSnapshot getById(long id) {

        return sqlSessionTemplate.selectOne(NAMESPACE + "selectById", id);
    }

    /**
     * 添加
     *
     * @param model
     * @return
     */
    public Long add(RiskSnapshot model) {
        int r = sqlSessionTemplate.insert(NAMESPACE + "insert", model);
        return model.getId() == null ? 0 : model.getId();
    }

    /**
     * 修改规则id
     *
     * @param id
     * @param ruleId
     * @return
     */
    public int updateRuleIdById(long id, long ruleId) {
        RiskSnapshot model = new RiskSnapshot();
        model.setId(id);
        model.setRuleId(ruleId);
        return sqlSessionTemplate.update(NAMESPACE + "updateRuleIdById", model);
    }

}
