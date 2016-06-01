package com.jd.jmi.escort.util;

import com.jd.jmi.escort.common.enums.*;

/**
 * Created by changpan on 2016/1/13.
 */
public class JmiEnumUtils {

    /**
     * 操作符
     * @return
     */
    public Operator[] getOperatorEnums(){
        return Operator.values();
    }

    /**
     * 操作符
     * @return
     */
    public Operator[] getOftenOperatorEnums(){
        return Operator.getOftenOperators();
    }

    /**
     * 会员等级
     * @return
     */
    public MemberGradeEnum[] getmMemberGradeEnums(){
        return MemberGradeEnum.getMemberGrades();
    }

    /**
     * 询问 是否
     * @return
     */
    public AskEnum[] getaAskEnums(){
        return AskEnum.values();
    }

    /**
     * 行为时段
     * @return
     */
    public TimeIntervalEnum[] getTimeIntervalEnums(){
        return TimeIntervalEnum.values();
    }

    /**
     * 黑名单同步规则时段
     * @return
     */
    public SyncTimeIntervalEnum[] getSyncTimeIntervalEnums(){
        return SyncTimeIntervalEnum.values();
    }

    /**
     * 订单状态
     * @return
     */
    public JmiOrderStatusEnum[] getJmiOrderStatusEnums(){
        return JmiOrderStatusEnum.values();
    }

    /**
     * 执行规则
     * @return
     */
    public JmiActionEnum[] getJmiActionEnums(){
        return JmiActionEnum.values();
    }

    /**
     * 起飞时间
     * @return
     */
    public TakeOffTimeEnum[] getTakeOffTimeEnums(){
            return TakeOffTimeEnum.values();
    }

    /**
     * 试用类型
     * @return
     */
    public TryTypeEnum[] gettTryTypeEnums(){
        return TryTypeEnum.values();
    }
}
