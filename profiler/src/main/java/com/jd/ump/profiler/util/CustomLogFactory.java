package com.jd.ump.profiler.util;

import com.jd.ump.log4j.*;
import com.jd.ump.log4j.spi.LoggerRepository;
import com.jd.ump.log4j.spi.RootLogger;
import com.jd.ump.profiler.common.UpdateModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomLogFactory {
    // 日志仓库实例
    private static LoggerRepository h = new Hierarchy(new RootLogger(Level.INFO));

    private static final String DEFAULTPATH = "/export/home/tomcat/UMP-Monitor";
    private static String MaxFileSize = 50 + "MB";
    private static int MaxBackupIndex = 3;
    private static String logPath = null;

    private static String TIMESTAMP = getTimeStamp();
    private static int PID = getPid();
    private static int RANDOM_CODE = getRandomCode();

    private static long CHECK_FILE_REMOVED_PERIOD = 2000L;

    static {
        Properties conf = new Properties();
        Properties props = null;
        InputStream is = null;
        try {
            is = CacheUtil.class.getResourceAsStream("/config.properties");
            if (is != null) {
                conf.load(is);
                logPath = conf.getProperty("jiankonglogPath", DEFAULTPATH);
                if (logPath.equals("")) {
                    logPath = DEFAULTPATH;
                }
            } else {
                logPath = DEFAULTPATH;
            }

            logPath = logPath + File.separator + "logs" + File.separator;
            File ump_root_path = new File(logPath);
            if (ump_root_path.exists() && ump_root_path.isDirectory()) {
                props = InitLog4jProperties(logPath);
            } else {
                ump_root_path.mkdir();
                props = InitLog4jProperties(logPath);
            }
            // 配置文件定制日志仓库属性，这里面你可以做更多的文章
            // 来定义自己的配置文件位置等等

            new PropertyConfigurator().doConfigure(props, h);

            Timer timer = new Timer("UMP-ProfilerFileUpdateThread", true);
            timer.scheduleAtFixedRate(new UpdateModule(), CacheUtil.UPDATETIME, CacheUtil.UPDATETIME);

            Timer checkFileRemovedTimer = new Timer("UMP-CheckFileRemovedThread", true);
            checkFileRemovedTimer.schedule(new CheckFileRemovedTask(), CHECK_FILE_REMOVED_PERIOD, CHECK_FILE_REMOVED_PERIOD);
        } catch (Throwable e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable ex) {
                    // ignore
                }
            }
        }
    }

    /**
     * 获取本JVM进程的ID
     *
     * @return JVM进程的ID
     */
    private static int getPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            // format: "pid@hostname" 
            String name = runtime.getName();
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable e) {
            return new Random().nextInt(50000) + 9900000;
        }
    }

    /**
     * 获取1,000,000内的一个随机数
     *
     * @return 随机数
     */
    private static int getRandomCode() {
        return new Random().nextInt(1000000);
    }

    /**
     * 获取时间戳
     *
     * @return 时间戳
     */
    private static String getTimeStamp() {
        TimeZone localTimeZone = TimeZone.getTimeZone("GMT+8");
        DateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
        format.setTimeZone(localTimeZone);
        return format.format(new Date());
    }

    public static Logger getLogger(String loggerName) {
        return h.getLogger(loggerName);
    }

    private static Properties InitLog4jProperties(String path) {

        Properties properties = new Properties();

        String tpLogFile = createFileName("tp.log");
        String aliveLogFile = createFileName("alive.log");
        String businessLogFile = createFileName("business.log");
        String bizLogFile = createFileName("biz.log");
        String jvmLogFile = createFileName("jvm.log");
        String commonLogFile = createFileName("common.log");

        setProperties(properties, "tpLogger", "A1", tpLogFile);
        setProperties(properties, "aliveLogger", "A2", aliveLogFile);
        setProperties(properties, "businessLogger", "A3", businessLogFile);
        setProperties(properties, "bizLogger", "A4", bizLogFile);
        setProperties(properties, "jvmLogger", "A5", jvmLogFile);
        setProperties(properties, "commonLogger", "A6", commonLogFile);

        return properties;
    }

    private static void setProperties(Properties properties, String loggerName, String appenderName, String fileName) {
        properties.setProperty(String.format("log4j.logger.%s", loggerName), String.format("INFO,%s", appenderName));
        properties.setProperty(String.format("log4j.appender.%s", appenderName), "com.jd.ump.log4j.RollingFileAppender");
        properties.setProperty(String.format("log4j.appender.%s.File", appenderName), fileName);
        properties.setProperty(String.format("log4j.appender.%s.MaxFileSize", appenderName), MaxFileSize);
        properties.setProperty(String.format("log4j.appender.%s.MaxBackupIndex", appenderName), String.valueOf(MaxBackupIndex));
        properties.setProperty(String.format("log4j.appender.%s.layout", appenderName), "com.jd.ump.log4j.SimpleLayout");
        properties.setProperty(String.format("log4j.appender.%s.encoding", appenderName), "UTF-8");
    }

    /**
     * 日志文件命名方式：进程启动时间戳+进程号+随机数
     *
     * @param name
     * @return
     */
    private static String createFileName(String name) {
        return logPath + TIMESTAMP + "_" + PID + "_" + RANDOM_CODE + "_" + name;
    }

    private static int tpLoggerFileRemovedCount = 0;
    private static int aliveLoggerFileRemovedCount = 0;
    private static int businessLoggerFileRemovedCount = 0;
    private static int bizLoggerFileRemovedCount = 0;
    private static int jvmLoggerFileRemovedCount = 0;
    private static int commonLoggerFileRemovedCount = 0;

    /**
     * 检测log文件是否被删除的时间任务
     *
     * @author chenzehong
     */
    static class CheckFileRemovedTask extends TimerTask {
        @Override
        public void run() {
            try {
                checkFileRemoved(CustomLogger.TpLogger.getLogger(), "tp.log", "A1");
                checkFileRemoved(CustomLogger.AliveLogger.getLogger(), "alive.log", "A2");
                checkFileRemoved(CustomLogger.BusinessLogger.getLogger(), "business.log", "A3");
                checkFileRemoved(CustomLogger.BizLogger.getLogger(), "biz.log", "A4");
                checkFileRemoved(CustomLogger.JVMLogger.getLogger(), "jvm.log", "A5");
                checkFileRemoved(CustomLogger.CommonLogger.getLogger(), "common.log", "A6");
            } catch (Throwable e) {
            }
        }

        /**
         * 检测日志文件是否被删除，并及时恢复
         *
         * @param logger       指定的log4j的logger对象
         * @param logName      指定的log文件名后缀
         * @param appenderName 指定的log4j的appender名字
         * @throws IOException
         */
        private void checkFileRemoved(Logger logger, String logName, String appenderName) throws IOException {
            RollingFileAppender appender = (RollingFileAppender) logger.getAppender(appenderName);

            if (appender != null) {
                String oldFile = appender.getFile();
                File file = new File(oldFile);
                if (file.exists()) {
                    // 日志文件还存在时对计数器归零
                    setCount2Zero(logName);
                } else {
                    // 日志文件不存在时对计数器+1
                    incrCount(logName);
                }

                // 如果连续两次检查出日志文件被删除，就执行日志文件恢复
                if (getCount(logName) >= 2) {
                    String newFile = createFileName(logName);

                    // 创建新的appender
                    RollingFileAppender newAppender;

                    SimpleLayout layout = new SimpleLayout();

                    newAppender = new RollingFileAppender(layout, newFile);
                    newAppender.setName(appenderName);
                    newAppender.setMaxFileSize(MaxFileSize);
                    newAppender.setMaxBackupIndex(MaxBackupIndex);
                    newAppender.setEncoding("UTF-8");

                    // 移除老的appender，加入新的appender
                    logger.removeAppender(appenderName);
                    logger.addAppender(newAppender);

                    // 恢复日志文件后对计数器归零
                    setCount2Zero(logName);
                }
            }
        }

        /**
         * 获取指定的日志删除计数器值
         *
         * @param logName 指定的日志文件名后缀
         * @return 指定的日志删除计数器值
         */
        private int getCount(String logName) {
            if ("tp.log".equals(logName)) {
                return tpLoggerFileRemovedCount;
            }

            if ("alive.log".equals(logName)) {
                return aliveLoggerFileRemovedCount;
            }

            if ("business.log".equals(logName)) {
                return businessLoggerFileRemovedCount;
            }

            if ("biz.log".equals(logName)) {
                return bizLoggerFileRemovedCount;
            }

            if ("jvm.log".equals(logName)) {
                return jvmLoggerFileRemovedCount;
            }

            if ("common.log".equals(logName)) {
                return commonLoggerFileRemovedCount;
            }

            return 0;
        }

        /**
         * 让指定的日志删除计数器值归零
         *
         * @param logName 指定的日志文件名后缀
         */
        private void setCount2Zero(String logName) {
            if ("tp.log".equals(logName)) {
                tpLoggerFileRemovedCount = 0;
            }

            if ("alive.log".equals(logName)) {
                aliveLoggerFileRemovedCount = 0;
            }

            if ("business.log".equals(logName)) {
                businessLoggerFileRemovedCount = 0;
            }

            if ("biz.log".equals(logName)) {
                bizLoggerFileRemovedCount = 0;
            }

            if ("jvm.log".equals(logName)) {
                jvmLoggerFileRemovedCount = 0;
            }

            if ("common.log".equals(logName)) {
                commonLoggerFileRemovedCount = 0;
            }
        }

        /**
         * 让指定的日志删除计数器值+1
         *
         * @param logName 指定的日志文件名后缀
         */
        private void incrCount(String logName) {
            if ("tp.log".equals(logName)) {
                tpLoggerFileRemovedCount++;
            }

            if ("alive.log".equals(logName)) {
                aliveLoggerFileRemovedCount++;
            }

            if ("business.log".equals(logName)) {
                businessLoggerFileRemovedCount++;
            }

            if ("biz.log".equals(logName)) {
                bizLoggerFileRemovedCount++;
            }

            if ("jvm.log".equals(logName)) {
                jvmLoggerFileRemovedCount++;
            }

            if ("common.log".equals(logName)) {
                commonLoggerFileRemovedCount++;
            }
        }
    }
}