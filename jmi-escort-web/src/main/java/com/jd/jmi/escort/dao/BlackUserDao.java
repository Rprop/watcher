package com.jd.jmi.escort.dao;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.pojo.BlackUser;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by changpan on 2016/2/16.
 */
@Repository
public class BlackUserDao {

    public static final String NAMESPACE = "com.jd.jmi.escort.dao.BlackUserMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public BlackUser getById(long id) {
        return sqlSessionTemplate.selectOne(NAMESPACE+"selectById",id);
    }

    /**
     * 添加
     * @param blackUser
     * @return
     */
    public int insert(BlackUser blackUser){
        return sqlSessionTemplate.insert(NAMESPACE+"insert",blackUser);
    }

    /**
     * 批量添加
     * @param users
     * @return
     */
    public int insertBatch(List<BlackUser> users) throws SQLException{
        return sqlSessionTemplate.insert(NAMESPACE+"insertBatch",users);
    }
    /**
     * 修改
     * @param blackUser
     * @return
     */
    public int update(BlackUser blackUser){
        return sqlSessionTemplate.update(NAMESPACE+"updateById",blackUser);
    }

    /**
     * 批量更新为黑名单
     * @param users
     * @return
     */
    public int updateLevelBatch(List<BlackUser> users){
        return sqlSessionTemplate.update(NAMESPACE+"updateLevelBatch",users);
    }

    /**
     * 触发次数+1
     * @param blackUser
     * @return
     */
    public int updateTriggerCountById(BlackUser blackUser){
        return sqlSessionTemplate.update(NAMESPACE+"updateTriggerCountById",blackUser);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(long id){
        return sqlSessionTemplate.delete(NAMESPACE+"deleteById",id);
    }

    public PaginatedList<BlackUser> list(BlackUserQuery blackUserQuery){
        PaginatedList<BlackUser> page = new PaginatedArrayList<BlackUser>(blackUserQuery.getPage(),
                blackUserQuery.getPageSize());
        int count = (Integer)sqlSessionTemplate.selectOne(NAMESPACE + "queryCount", blackUserQuery);
        page.setTotalItem(count);
        if(count >0){
            blackUserQuery.setStartRow(page.getStartRow() - 1);
            page.addAll((List)sqlSessionTemplate.selectList(NAMESPACE + "queryList", blackUserQuery));
        }
        return page;
    }

    /**
     * 根据用户pin和orderType查询
     * @param blackUserQuery
     * @return
     */
    public BlackUser selectByNameOrderType(BlackUserQuery blackUserQuery) {
        return sqlSessionTemplate.selectOne(NAMESPACE+"selectByNameOrderType",blackUserQuery);
    }


}
