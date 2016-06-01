package com.jd.jmi.escort.service;

import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.pojo.WhiteUser;

/**
 * @author changpan
 * @version 1.0
 * @date 2016/2/23
 */
public interface WhiteUserService {

    public PaginatedList<WhiteUser> list(BlackUserQuery blackUserQuery);

    /**
     * 删除
     * @param id
     * @param uname
     * @return
     */
    public Result delete(long id, String uname);

    /**
     * 添加白名单用户
     * @param user
     * @return
     */
    public Result insert(WhiteUser user);

    /**
     * 批量插入用户
     * @param userPin
     * @param orderTypes
     * @param sourceEnum
     * @param operateUser
     * @return
     */
    public Result insertBatch(String userPin,Integer[] orderTypes,SourceEnum sourceEnum,String operateUser);

    /**
     * 通过用户名和订单类型获取白名单用户
     *
     * @param userPin
     * @param orderType
     * @return
     */
    public WhiteUser selectByNameOrderType(String userPin, Integer orderType);


}
