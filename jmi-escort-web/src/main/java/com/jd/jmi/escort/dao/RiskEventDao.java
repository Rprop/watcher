package com.jd.jmi.escort.dao;

import com.jd.jmi.escort.common.util.StringUtils;
import com.jd.jmi.escort.model.RiskEventModel;
import com.jd.jmi.escort.pojo.RiskEvent;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyongang on 2016/2/16.
 * 风险事件dao
 */
@Repository
public class RiskEventDao {

    public static final String NAMESPACE = "com.jd.jmi.escort.dao.RiskEventMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public RiskEvent getById(long id) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectById", id);
    }

    public List<RiskEvent> getAll() {
        return sqlSessionTemplate.selectList(NAMESPACE + "getAll");
    }

    /**
     * 添加风险事件
     *
     * @param model
     * @return
     */
    public int insertRiskEvent(RiskEvent model) {
        return sqlSessionTemplate.insert(NAMESPACE + "insert", model);
    }

    /**
     * 获取风险事件
     *
     * @param model
     * @return
     */
    public List<RiskEvent> getRiskEventList(RiskEventModel model) {
        return sqlSessionTemplate.selectList(NAMESPACE + "getRiskEventList", model);
    }

    public int countJmiPayokBury(RiskEventModel model) {
        return (Integer) sqlSessionTemplate.selectOne(NAMESPACE + "countRiskEvent", model);

    }

    public String countRiskEventTotal(RiskEventModel model) {
        return (String) sqlSessionTemplate.selectOne(NAMESPACE + "countRiskEventTotal", model);

    }

    public int updateForDelUser(String userPin, Integer isDelUser, Integer orderType) throws Exception {
        if (StringUtils.isEmpty(userPin) || null == isDelUser || null == orderType) {

            throw new Exception("参数错误， 无法更新");
        }
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("userPin", userPin);
        map.put("isDelUser", isDelUser);
        map.put("orderType", orderType);
        return (int) sqlSessionTemplate.update(NAMESPACE + "updateForDelUser", map);

    }

    public int updateForBlackUserLevel(Long id, Integer blackUserLevel) throws Exception {
        if (null == id || id < 0 || null == blackUserLevel) {

            throw new Exception("参数错误， 无法更新");
        }
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id", id);
        map.put("blackUserLevel", blackUserLevel);
        return (int) sqlSessionTemplate.update(NAMESPACE + "updateForBlackUserLevel", map);

    }


}
