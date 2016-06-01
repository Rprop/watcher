package com.jd.jmi.escort.service;

import com.jd.user.sdk.export.domain.UserBaseInfoVo;

/**
 * Created by changpan on 2016/2/23.
 */
public interface UserService {

    /**
     * 获取用户基本信息
     * @param userPin
     * @return
     */
    public UserBaseInfoVo getUserinfo(String userPin);

    /**
     * 获取用户风险等级
     * @param userPin
     * @return
     */
    public Double getRisk(String userPin);
}
