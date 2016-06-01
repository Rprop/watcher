package com.jd.jmi.escort.service.impl;

import com.jd.jmi.escort.common.enums.BlackUserLevelEnum;
import com.jd.jmi.escort.common.model.risk.RiskUserStatusVO;
import com.jd.jmi.escort.common.service.JmiRiskUserJsfService;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.pojo.WhiteUser;
import com.jd.jmi.escort.service.BlackUserService;
import com.jd.jmi.escort.service.WhiteUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by changpan on 2016/1/28.
 */
@Service("jmiRiskUserJsfService")
public class JmiRiskUserJsfServiceImpl implements JmiRiskUserJsfService {

    @Resource
    private BlackUserService blackUserService;

    @Resource
    private WhiteUserService whiteUserService;

    @Override
    public boolean isBlackUser(String uname, int orderType) {
        BlackUser user = blackUserService.selectByNameOrderType(uname, orderType);
        if (user != null && user.getLevel() == BlackUserLevelEnum.BLACKUSER.getCode()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isWhiteUser(String uname, int orderType) {
        WhiteUser user = whiteUserService.selectByNameOrderType(uname, orderType);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public RiskUserStatusVO getRiskUserStatus(String uname, int orderType) {
        RiskUserStatusVO riskUserStatusVO = new RiskUserStatusVO();
        boolean isWhiteUser = isWhiteUser(uname, orderType);
        riskUserStatusVO.setWhite(isWhiteUser);
        if (!isWhiteUser) {
            riskUserStatusVO.setBlack(isBlackUser(uname, orderType));
        }
        return riskUserStatusVO;
    }
}
