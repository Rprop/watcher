package com.jd.jmi.escort.util;

import com.jd.common.web.LoginContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WebHelper {
    /**
     * 获取当前登录人的ID
     *
     * @return long
     */
    public static final long getUserId() {
        long userId = 0;
        if (LoginContext.getLoginContext() != null) {
            userId = LoginContext.getLoginContext().getUserId();
        }
        return userId;
    }

    /**
     * 获取当前登录登录人的登入账号
     *
     * @return long
     */
    public static final String getPin() {
        String userPin = null;
        if (LoginContext.getLoginContext() != null) {
            userPin = LoginContext.getLoginContext().getPin();
        }
        return userPin;
    }

    /**
     * 获取当前登录登录人的显示名称
     */
    public static final String getNick() {
        String userNick = null;
        if (LoginContext.getLoginContext() != null) {
            userNick = LoginContext.getLoginContext().getNick();
        }
        return userNick;
    }

    /**
     * 获取当前登录登录人登录信息
     *
     * @return
     */
    public static final Map<String, Object> getLoginInfo() {
        Map<String, Object> loginInfo = new HashMap<String, Object>();
        loginInfo.put("userId", getUserId());
        loginInfo.put("pin", getPin());
        loginInfo.put("nick", getNick());
        return loginInfo;
    }
}
