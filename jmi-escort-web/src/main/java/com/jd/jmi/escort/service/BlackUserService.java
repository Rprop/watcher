package com.jd.jmi.escort.service;

import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.pojo.BlackUser;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/18
 */
public interface BlackUserService {

    public PaginatedList<BlackUser> list(BlackUserQuery blackUserQuery);

    /**
     * 保存
     *
     * @param blackUser
     * @return
     */
    public Result insert(BlackUser blackUser);

    /**
     * 修改
     *
     * @param blackUser
     * @return
     */
    public Result update(BlackUser blackUser);

    /**
     * 批量保存
     *
     * @param userPin
     * @param orderTypes
     * @param sourceEnum
     * @param operateUser
     * @return
     */
    public Result insertBatch(String userPin, Integer[] orderTypes, SourceEnum sourceEnum, String operateUser);

    /**
     * 合并
     *
     * @param userPin
     * @param orderType
     * @param level
     * @return
     */
    public Result merge(String userPin, Integer orderType, Integer level);

    /**
     * 根据用户pin和orderType查询
     *
     * @param userPin
     * @param orderType
     * @return
     */
    public BlackUser selectByNameOrderType(String userPin, Integer orderType);

    /**
     * 删除
     *
     * @param id
     * @param uname
     * @return
     */
    public Result delete(long id, String uname);


}
