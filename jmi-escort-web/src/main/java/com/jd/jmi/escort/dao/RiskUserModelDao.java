package com.jd.jmi.escort.dao;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.pojo.RiskDecideModel;
import com.jd.jmi.escort.pojo.RiskEffectRule;
import com.jd.jmi.escort.pojo.RiskUserModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by changpan on 2015/12/28.
 */
@Repository
public class RiskUserModelDao {

    public static final String NAMESPACE = "com.jd.jmi.escort.dao.RiskUserModel.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public RiskUserModel getById(long id) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectById", id);
    }

    public PaginatedList<RiskUserModel> list(RiskUserModel model) {
        PaginatedList<RiskUserModel> page = new PaginatedArrayList<RiskUserModel>(model.getPage(),
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
     * @param riskUserModel
     * @return
     */
    public long add(RiskUserModel riskUserModel) {
        int r = sqlSessionTemplate.insert(NAMESPACE + "insert", riskUserModel);
        return (r == 1 && riskUserModel.getId() != null) ? riskUserModel.getId() : 0;
    }

    /**
     * 修改
     *
     * @param riskUserModel
     * @return
     */
    public int update(RiskUserModel riskUserModel) {
        return sqlSessionTemplate.update(NAMESPACE + "updateById", riskUserModel);
    }

    public int updateStatus(RiskUserModel riskUserModel) {
        return sqlSessionTemplate.update(NAMESPACE + "updateStatusById", riskUserModel);
    }

    /**
     * 获取可用的用户模型
     *
     * @param orderType
     * @return
     */
    public List<RiskUserModel> getEnabledByOrderType(int orderType) {
        return sqlSessionTemplate.selectList(NAMESPACE + "getEnabledByOrderType", orderType);
    }
}
