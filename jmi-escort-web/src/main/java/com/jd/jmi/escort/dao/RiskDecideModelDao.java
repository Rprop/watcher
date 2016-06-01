package com.jd.jmi.escort.dao;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskUserModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by changpan on 2015/12/28.
 * 风险判定模型dao
 */
@Repository
public class RiskDecideModelDao {

    public static final String NAMESPACE = "com.jd.jmi.escort.dao.RiskDecideModelMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public RiskDecideModel getById(long id) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectById", id);
    }

    public PaginatedList<RiskDecideModel> list(RiskDecideModel model) {
        PaginatedList<RiskDecideModel> page = new PaginatedArrayList<RiskDecideModel>(model.getPage(),
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
    public long add(RiskDecideModel model) {
        int r = sqlSessionTemplate.insert(NAMESPACE + "insert", model);
        return (r == 1 && model.getId() != null) ? model.getId() : 0;
    }

    /**
     * 修改
     *
     * @param model
     * @return
     */
    public int update(RiskDecideModel model) {
        return sqlSessionTemplate.update(NAMESPACE + "updateById", model);
    }

    /**
     * 更改状态
     *
     * @param model
     * @return
     */
    public int updateStatus(RiskDecideModel model) {
        return sqlSessionTemplate.update(NAMESPACE + "updateStatusById", model);
    }

    /**
     * 获取可用的用户模型
     *
     * @param orderType
     * @return
     */
    public List<RiskDecideModel> getEnabledByOrderType(int orderType) {
        return sqlSessionTemplate.selectList(NAMESPACE + "getEnabledByOrderType", orderType);
    }
}
