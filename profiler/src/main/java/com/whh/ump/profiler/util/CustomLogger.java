package com.whh.ump.profiler.util;

/**
 * 获取自定义的Logger
 *
 * @author chen peng
 */

import com.jd.ump.log4j.Logger;

public class CustomLogger {
    public final static CustomLogger TpLogger = new CustomLogger(CustomLogFactory.getLogger("tpLogger"));
    public final static CustomLogger AliveLogger = new CustomLogger(CustomLogFactory.getLogger("aliveLogger"));
    public final static CustomLogger BusinessLogger = new CustomLogger(CustomLogFactory.getLogger("businessLogger"));
    public final static CustomLogger BizLogger = new CustomLogger(CustomLogFactory.getLogger("bizLogger"));
    public final static CustomLogger JVMLogger = new CustomLogger(CustomLogFactory.getLogger("jvmLogger"));
    public final static CustomLogger CommonLogger = new CustomLogger(CustomLogFactory.getLogger("commonLogger"));

    private Logger logger;

    public CustomLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String message) {
        logger.info(message);
    }

    public Logger getLogger() {
        return logger;
    }
}