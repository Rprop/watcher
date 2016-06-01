package com.whh.ump.profiler.proxy;

import com.whh.ump.profiler.CallerInfo;
import com.whh.ump.profiler.ProfilerImpl;

import java.util.Map;

public class Profiler {
    /**
     * 方法监控点开始方法,调用此方法只对方法的心跳进行监控，页面上将不会展现方法性能曲线。如果想进行方法性能监控，请使用registerInfo(
     * String,boolean,boolean)方法
     *
     * @param key 在http://ump.360buy.com上注册的方法监控点的key
     * @see registerInfo(String key,boolean enableHeartbeat,boolean enableTP)。
     */
    public static CallerInfo registerInfo(String key) {
        CallerInfo callerInfo = null;
        if (key != null && !"".equals(key.trim())) {
            callerInfo = ProfilerImpl.scopeStart(key.trim(), true, false);
        }
        return callerInfo;
    }

    /**
     * 方法监控点开始方法,调用此方法可对方法的性能或心跳进行监控。要完成方法性能监控，需配套使用registerInfoEnd方法。
     *
     * @param key             在http://ump.360buy.com上注册的方法监控点的key
     * @param enableHeartbeat true表示开启方法心跳监控，false则关闭
     * @param enableTP        true表示开启方法性能监控，false则关闭
     * @return
     */
    public static CallerInfo registerInfo(String key, boolean enableHeartbeat, boolean enableTP) {
        CallerInfo callerInfo = null;
        if (key != null && !"".equals(key.trim())) {
            callerInfo = ProfilerImpl.scopeStart(key.trim(), enableHeartbeat, enableTP);
        }
        return callerInfo;
    }

    public static CallerInfo registerInfo(String key, String appName, boolean enableHeartbeat, boolean enableTP) {
        CallerInfo callerInfo = null;
        if (key != null && !"".equals(key.trim()) && appName != null && !"".equals(appName.trim())) {
            callerInfo = ProfilerImpl.scopeStart(key.trim(), appName.trim(), enableHeartbeat, enableTP);
        }
        return callerInfo;
    }

    /**
     * 方法监控点结束方法，调用此方法才能完成方法性能的监控
     *
     * @param callerInfo :registerInfo方法返回的CallerInfo对象
     */
    public static void registerInfoEnd(CallerInfo callerInfo) {
        ProfilerImpl.scopeEnd(callerInfo);
    }

    /**
     * 调用此方法将传递报警信息到监控系统直接报警<br>
     * 当某中业务产生的事件需要直接报警，可以调用此方法。
     *
     * @param key    :在http://ump.360buy.com上注册的自定义监控点的key,为空则不会报警
     * @param detail :报警信息详细描述,允许的最大长度为512字符,为空则不会报警
     */
    public static void businessAlarm(String key, String detail) {
        if (key != null && !"".equals(key.trim()) && detail != null && !"".equals(detail.trim())) {
            ProfilerImpl.registerBusiness(key.trim(), 0, 0, detail.trim());
        }
    }

    /**
     * 调用此方法将传递报警信息到监控系统直接报警<br>
     * 当某中业务产生的事件需要直接报警，可以调用此方法。
     *
     * @param key
     * @param time
     * @param detail
     * @Title: businessAlarm
     * @Description:
     */
    public static void businessAlarm(String key, long time, String detail) {
        if (key != null && !"".equals(key.trim()) && detail != null && !"".equals(detail.trim())) {
            ProfilerImpl.registerBusiness(key.trim(), 0, 0, detail.trim());
        }
    }

    /**
     * 调用此方法将传递报警信息到监控系统直接报警<br>
     * 当某中业务产生的事件需要直接报警，可以调用此方法。
     *
     * @param key      :在http://ump.360buy.com上注册的自定义监控点的key,为空则不会报警
     * @param detail   :报警信息详细描述,允许的最大长度为512字符,为空则不会报警
     * @param rtxList  :配置的RTX帐号列表,使用","分割帐号.eg:xxxx,xxxx,xxxx
     * @param mailList :配置的邮箱帐号列表,使用","分割帐号.eg:xx@xx.com,xx@xx.com,xx@xx.com
     * @param smsList  :配置的手机号列表,使用","分割帐号.eg:187xxxx,135xxxxx,159xxxxx,
     */
    public static void businessAlarm(String key, String detail, String rtxList, String mailList, String smsList) {
        if (key != null && !"".equals(key.trim()) && detail != null && !"".equals(detail.trim())) {
            ProfilerImpl.registerBusiness(key.trim(), 5, 0, detail.trim(), rtxList, mailList, smsList);
        }
    }

    /**
     * 调用此方法将传递报警信息到监控系统直接报警<br>
     * 当某中业务产生的事件需要直接报警，可以调用此方法。
     *
     * @param key
     * @param time
     * @param detail
     * @param rtxList
     * @param mailList
     * @param smsList
     * @Title: businessAlarm
     * @Description:
     */
    public static void businessAlarm(String key, long time, String detail, String rtxList, String mailList, String smsList) {
        if (key != null && !"".equals(key.trim()) && detail != null && !"".equals(detail.trim())) {
            ProfilerImpl.registerBusiness(key.trim(), 5, 0, detail.trim(), rtxList, mailList, smsList);
        }
    }

    /**
     * 系统级心跳启动方法,调用此方法将对应用系统进行心跳监控.每个系统只需调用一次,多次调用仅第一次有效。
     *
     * @param key 在http://ump.360buy.com上注册的方法监控点的key
     */
    public static void InitHeartBeats(String key) {
        if (key != null && !"".equals(key.trim())) {
            ProfilerImpl.scopeAlive(key.trim());
        }

    }

    /**
     * 调用此方法系统将会统计方法的可用率,此方法建议在抛出异常或业务逻辑出现异常时调用
     *
     * @param callerInfo
     */
    public static void functionError(CallerInfo callerInfo) {
        ProfilerImpl.scopeFunctionError(callerInfo);
    }

    /**
     * 调用此方法将记录业务传递的值，在配置的时间内计算值的总和，达到配置的阀值将进行报警<br>
     * 当某中业务产生的事件需要记录值并在配置的时间内值的总和不能超过阀值是，可以调用此方法。
     *
     * @param key   在http://ump.360buy.com上注册的自定义监控点的key
     * @param value 本次业务需要传入的值
     */
    public static void valueAccumulate(String key, Number bValue) {
        if (key != null && !"".equals(key.trim()) && bValue != null) {
            ProfilerImpl.registerBizData(key.trim(), 1, bValue);
        }
    }

    /**
     * 调用此方法将记录一次业务请求，在配置的时间内计算总次数，达到配置的阀值将进行报警<br>
     * 当某种业务产生的事件需要记录次数并在规定时间内不能超过预定次数，可以调用此方法。
     *
     * @param key :在http://ump.360buy.com上注册的自定义监控点的key
     */
    public static void countAccumulate(String key) {
        if (key != null && !"".equals(key.trim())) {
            ProfilerImpl.registerBizData(key.trim(), 2, 1);
        }
    }

    /**
     * 调用此方法，传入需要监控的原始业务数据，系统会根据在网站上配置的报警规则进行分析报警和数据展示<br>
     * 当需要对业务数据进行监控，同时根据业务需求需要报警和数据分析，可以调用此方法。
     *
     * @param key     :在http://ump.360buy.com上注册的自定义监控点的key
     * @param dataMap :需要监控的业务数据，业务值类型为数值型
     */
    public static void sourceDataByNum(String key, Map<String, Number> dataMap) {
        if (key != null && !"".equals(key.trim()) && dataMap != null && !dataMap.isEmpty()) {
            ProfilerImpl.registerBizData(key.trim(), 3, dataMap);
        }
    }

    /**
     * 调用此方法，传入需要监控的原始业务数据，系统会根据在网站上配置的报警规则进行分析报警和数据展示<br>
     * 当需要对业务数据进行监控，同时根据业务需求需要报警和数据分析，可以调用此方法。
     *
     * @param key     :在http://ump.360buy.com上注册的自定义监控点的key
     * @param dataMap :需要监控的业务数据，业务值类型为字符串型
     */
    public static void sourceDataByStr(String key, Map<String, String> dataMap) {
        if (key != null && !"".equals(key.trim()) && dataMap != null && !dataMap.isEmpty()) {
            ProfilerImpl.registerBizData(key.trim(), 3, dataMap);
        }
    }

    /**
     * 调用此方法将对jvm的运行情况进行监控，此方法建议在应用启动时调用，每个应用只需调用一次,多次调用仅第一次有效。<br>
     *
     * @param key :在http://ump.360buy.com上注册的自定义监控点的key
     */
    public static void registerJVMInfo(String key) {
        if (key != null && !"".equals(key.trim())) {
            ProfilerImpl.registerJvmData(key.trim());
        }
    }

    /**
     * 调用此方法将记录一条流程数据
     *
     * @param nodeId 流程节点标志
     * @param data   用户自定义数据项
     */
    public static void bizNode(String nodeID, Map<String, String> data) {
        try {
            if (nodeID != null && !"".equals(nodeID = nodeID.trim())
                    && data != null && !data.isEmpty()) {
                ProfilerImpl.bizNode(nodeID, data);
            }
        } catch (Throwable t) {
        }
    }

    /**
     * 调用此方法将记录一条流程数据
     *
     * @param nodeID 流程节点标志
     * @param data   用户自定义数据项， 标准的json格式
     */
    public static void bizNode(String nodeID, String data) {
        try {
            if (nodeID != null && !"".equals(nodeID = nodeID.trim())
                    && data != null && !"".equals(data = data.trim())) {
                ProfilerImpl.bizNode(nodeID, data);
            }
        } catch (Throwable t) {
        }
    }

    /**
     * 通用日志输出接口
     *
     * @param type 日志类型标识
     * @param data 日志数据，标准的json格式
     */
    public static void log(String type, String data) {
        try {
            if (type != null && !"".equals(type = type.trim())
                    && data != null && !"".equals(data = data.trim())) {

                ProfilerImpl.log(type, data);
            }
        } catch (Throwable t) {
        }
    }

    /**
     * 通用日志输出接口
     *
     * @param type 日志类型标识
     * @param data 日志数据
     */
    public static void log(String type, Map<String, String> data) {
        try {
            if (type != null && !"".equals(type = type.trim())
                    && data != null && !data.isEmpty()) {

                ProfilerImpl.log(type, data);
            }
        } catch (Throwable t) {
        }
    }
}