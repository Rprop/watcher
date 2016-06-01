package com.jd.jmi.escort.dao;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.pojo.WhiteUser;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by changpan on 2016/2/16.
 */
@Repository
public class WhiteUserDao {
    public static final String NAMESPACE = "com.jd.jmi.escort.dao.WhiteUserMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public WhiteUser getById(long id) {
        return sqlSessionTemplate.selectOne(NAMESPACE+"selectById",id);
    }

    /**
     * 添加
     * @param model
     * @return
     */
    public int add(WhiteUser model){
        return sqlSessionTemplate.insert(NAMESPACE+"insert",model);
    }

    /**
     * 修改
     * @param model
     * @return
     */
    public int update(WhiteUser model){
        return sqlSessionTemplate.update(NAMESPACE+"updateById",model);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(long id){
        return sqlSessionTemplate.delete(NAMESPACE+"deleteById",id);
    }

    public PaginatedList<WhiteUser> list(BlackUserQuery blackUserQuery){
        PaginatedList<WhiteUser> page = new PaginatedArrayList<WhiteUser>(blackUserQuery.getPage(),
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
     * 插入用户
     * @param user
     * @return
     */
    public int insert(WhiteUser user){
        return sqlSessionTemplate.insert(NAMESPACE + "insert", user);
    }

    /**
     * 批量插入用户
     * @param users
     * @return
     */
    public int insertBatch(List<WhiteUser> users){
        return sqlSessionTemplate.insert(NAMESPACE + "insertBatch", users);
    }
    /**
     * 根据用户pin和orderType查询
     * @param whiteUser
     * @return
     */
    public WhiteUser selectByNameOrderType(WhiteUser whiteUser) {
        return sqlSessionTemplate.selectOne(NAMESPACE+"selectByNameOrderType",whiteUser);
    }

}
