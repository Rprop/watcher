package com.jd.jmi.escort.dao;

import com.jd.jmi.escort.pojo.BlackSyncRule;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/16
 */
@Repository
public class BlackSyncRuleDao {

    public static final String NAMESPACE = "com.jd.jmi.escort.dao.BlackSyncRuleMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public BlackSyncRule getById(long id) {
        return sqlSessionTemplate.selectOne(NAMESPACE+"selectById",id);
    }

    public List<BlackSyncRule> getAll() {
        return sqlSessionTemplate.selectList(NAMESPACE+"getAll");
    }

    /**
     * 添加
     * @param syncRule
     * @return
     */
    public int add(BlackSyncRule syncRule){
        return sqlSessionTemplate.insert(NAMESPACE+"insert",syncRule);
    }

    /**
     * 修改
     * @param syncRule
     * @return
     */
    public int update(BlackSyncRule syncRule){
        return sqlSessionTemplate.update(NAMESPACE+"updateById",syncRule);
    }

    /**
     * 更改状态
     * @param syncRule
     * @return
     */
    public int updateStatus(BlackSyncRule syncRule){
        return sqlSessionTemplate.update(NAMESPACE+"updateStatusById",syncRule);
    }

    /**
     * 获取可用的同步规则配置
     * @param orderType
     * @return
     */
    public List<BlackSyncRule> getEnabledByOrderType(int orderType){
        return sqlSessionTemplate.selectList(NAMESPACE + "getEnabledByOrderType", orderType);
    }

    /**
     * 获取同步规则配置
     * @param orderType
     * @return
     */
    public List<BlackSyncRule> getByOrderType(int orderType){
        return sqlSessionTemplate.selectList(NAMESPACE + "getByOrderType", orderType);
    }
}
