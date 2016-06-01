package com.jd.watcher.util.jvm;

//import java.io.UnsupportedEncodingException;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

//import java.lang.reflect.Method;

/**
 * JVM 信息获取器的本地实现（运行于JVM内部）
 *
 * @author chenhualiang
 * @see JvmInfoPicker,java.lang.management.RuntimeMXBean, com.sun.management.OperatingSystemMXBean,MemoryMXBean,ThreadMXBean,ClassLoadingMXBean,System
 * @since 2013-01-28
 */
//@SuppressWarnings({"unchecked" })
public class LocalJvmInfoPicker implements JvmInfoPicker {

//	@SuppressWarnings("rawtypes")
//	private static Class SUM_GC_CLASS;
//
//	private static Method SUM_GC_METHOD;

//	static{
//		try {
//			SUM_GC_CLASS = Class.forName("sun.management.GarbageCollectorImpl");
//			SUM_GC_CLASS = Class.forName("com.sun.management.GarbageCollectorMXBean");
//			SUM_GC_METHOD = SUM_GC_CLASS.getMethod("getLastGcInfo", new Class[0]);
//			if(!SUM_GC_METHOD.isAccessible())
//			{
//				SUM_GC_METHOD.setAccessible(true);
//			}
//		} catch (Exception e) {
//		}
//	}

    /**
     * 双引号
     */
    private static final String QUOTATION = "\"";

    /**
     * 冒号
     */
    private static final String COLON = ":";

    /**
     * 逗号
     */
    private static final String COMMA = ",";

    /**
     * 最近一次更新cpu使用率的时间
     */
    private long uptime;

    /**
     * 最近一次更新cpu使用使用的时间
     */
    private long processCpuTime;

    /**
     * 新生代收集器
     */
    private GarbageCollectorMXBean youngGC;

    /**
     * 全GC收集器
     */
    private GarbageCollectorMXBean fullGC;

    /**
     * JVM 信息获取器的本地实现 的单利
     */
    private static LocalJvmInfoPicker instance = new LocalJvmInfoPicker();

    /**
     * 构造方法
     */
    private LocalJvmInfoPicker() {
        List<GarbageCollectorMXBean> gcList = ManagementFactory.getGarbageCollectorMXBeans();
        if (gcList == null || gcList.isEmpty()) {
            return;
        }
        if (gcList.size() == 1) {
            youngGC = gcList.get(0);
        } else if (gcList.size() >= 2) {
            youngGC = gcList.get(0);
            fullGC = gcList.get(1);
        }
    }

    /**
     * 获取JVM 信息获取器的本地实现 的单利
     *
     * @return JVM信息获取器的本地实现 的单利
     * @author chenhualiang
     */
    public static LocalJvmInfoPicker getInstance() {
        return instance;
    }

    /**
     * 获取新生代收集器的名字
     */
    public static String getYoungGCName() {
        if (instance.youngGC != null) {
            return instance.youngGC.getName();
        }
        return "NULL";
    }

    /**
     * 获取全GC收集器的名字
     */
    public static String getFullGCName() {
        if (instance.fullGC != null) {
            return instance.fullGC.getName();
        }
        return "NULL";
    }

    /**
     * 获取新生代收集器花销总时间
     */
    public static String getYoungGCTime() {
        if (instance.youngGC != null) {
            return String.valueOf(instance.youngGC.getCollectionTime());
        }
        return "0";
    }

    /**
     * 获取新生代收集器执行次数
     */
    public static String getYoungGCCount() {
        if (instance.youngGC != null) {
            return String.valueOf(instance.youngGC.getCollectionCount());
        }
        return "0";
    }

    /**
     * 获取全GC收集器花销总时间
     */
    public static String getFullGCTime() {
        if (instance.fullGC != null) {
            return String.valueOf(instance.fullGC.getCollectionTime());
        }
        return "0";
    }

    /**
     * 获取全GC收集器执行次数
     */
    public static String getFullGCCount() {
        if (instance.fullGC != null) {
            return String.valueOf(instance.fullGC.getCollectionCount());
        }
        return "0";
    }

    /**
     * 获取最后一次全GC收集器执行开始时间
     */
    public static Long getFullGCStartTime() {
//		if(instance.fullGC != null && SUM_GC_METHOD != null)
//		{
//			com.sun.management.GcInfo info = null;
//			try {
//				info =(com.sun.management.GcInfo)SUM_GC_METHOD.invoke(instance.fullGC);
//			} catch (Exception e) {
//			}
//			if(info != null)
//			{
//				return ManagementFactory.getRuntimeMXBean().getStartTime() + info.getStartTime();
//			}
//		}
        //JDK 6U25存在本地内存泄露问题，在JDK 6U31修复该问题；如果要启用上面注释掉的代码 请确认JDK版本在6U31及以上
        return null;
    }

    /**
     * 获取最后一次全GC收集器执行结束时间
     */
    public static Long getFullGCEndTime() {
//		if(instance.fullGC != null && SUM_GC_METHOD != null)
//		{
//			com.sun.management.GcInfo info = null;
//			try {
//				info =(com.sun.management.GcInfo)SUM_GC_METHOD.invoke(instance.fullGC);
//			} catch (Exception e) {
//			}
//			if(info != null)
//			{
//				return ManagementFactory.getRuntimeMXBean().getStartTime() + info.getEndTime();
//			}
//		}
        //JDK 6U25存在本地内存泄露问题，在JDK 6U31修复该问题；如果要启用上面注释掉的代码 请确认JDK版本在6U31及以上
        return null;
    }

    /**
     * 获取最后一次新生代收集器执行开始时间
     */
    public static Long getYoungGCStartTime() {
//		if(instance.youngGC != null && SUM_GC_METHOD != null)
//		{
//			com.sun.management.GcInfo info = null;
//			try {
//				info =(com.sun.management.GcInfo)SUM_GC_METHOD.invoke(instance.youngGC);
//			} catch (Exception e) {
//			}
//			if(info != null)
//			{
//				return ManagementFactory.getRuntimeMXBean().getStartTime() + info.getStartTime();
//			}
//		}
        //JDK 6U25存在本地内存泄露问题，在JDK 6U31修复该问题；如果要启用上面注释掉的代码 请确认JDK版本在6U31及以上
        return null;
    }

    /**
     * 获取最后一次新生代收集器执行结束时间
     */
    public static Long getYoungGCEndTime() {
//		if(instance.youngGC != null && SUM_GC_METHOD != null)
//		{
//			com.sun.management.GcInfo info = null;
//			try {
//				info =(com.sun.management.GcInfo)SUM_GC_METHOD.invoke(instance.youngGC);
//			} catch (Exception e) {
//			}
//			if(info != null)
//			{
//				return ManagementFactory.getRuntimeMXBean().getStartTime() + info.getEndTime();
//			}
//		}
        //JDK 6U25存在本地内存泄露问题，在JDK 6U31修复该问题；如果要启用上面注释掉的代码 请确认JDK版本在6U31及以上
        return null;
    }

    /**
     * 获取最后一次新生代收集器执行时间
     */
    public static Long getYoungGCDuration() {
//		if(instance.youngGC != null && SUM_GC_METHOD != null)
//		{
//			com.sun.management.GcInfo info = null;
//			try {
//				info =(com.sun.management.GcInfo)SUM_GC_METHOD.invoke(instance.youngGC);
//			} catch (Exception e) {
//			}
//			if(info != null)
//			{
//				return info.getDuration();
//			}
//		}
        //JDK 6U25存在本地内存泄露问题，在JDK 6U31修复该问题；如果要启用上面注释掉的代码 请确认JDK版本在6U31及以上
        return null;
    }

    /**
     * 获取最后一次全GC收集器执行时间
     */
    public static Long getFullGCDuration() {
//		if(instance.fullGC != null && SUM_GC_METHOD != null)
//		{
//			com.sun.management.GcInfo info = null;
//			try {
//				info =(com.sun.management.GcInfo)SUM_GC_METHOD.invoke(instance.fullGC);
//			} catch (Exception e) {
//			}
//			if(info != null)
//			{
//				return info.getDuration();
//			}
//		}
        //JDK 6U25存在本地内存泄露问题，在JDK 6U31修复该问题；如果要启用上面注释掉的代码 请确认JDK版本在6U31及以上
        return null;
    }

    /**
     * 获取体系结构
     */
    public String getOSArch() {
        return System.getProperties().getProperty("os.arch");
    }

    /**
     * 获取操作系统名称
     */
    public String getOSName() {
        return System.getProperties().getProperty("os.name");
    }

    /**
     * 操作系统的体制
     */
    public String getSystemModel() {
        return System.getProperties().getProperty("sun.arch.data.model");
    }

    /**
     * 库路径
     */
    public String getLibPath() {
        return System.getProperties().getProperty("java.library.path");
    }

    /**
     * jre的版本号
     */
    public String getJREVersion() {
        return System.getProperties().getProperty("java.version");
    }

    /**
     * 得到启动路径的日志配置文件
     * @return
     */
    public String getServerLogConfigFile() {
        return System.getProperties().getProperty("java.util.logging.config.file");
    }

    /**
     * 得到服务器的唯一ID，不启动tomcat的需单独设置
     * @return
     */
    public String getServerId() {
    	//默认使用启动路径的日志配置文件
    	String serverId = getServerLogConfigFile();
    	if(serverId==null || "".equals(serverId)){
    		serverId = System.getProperties().getProperty("server.id");
    		if(serverId==null){
    			serverId = "";
    		}
    	}
        return serverId;
    }

    /**
     * 系统启动时间
     * <p/>
     * 格式：yyyy-MM-dd HH:mm:ss.SSS
     */
    public String getStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(ManagementFactory.getRuntimeMXBean().getStartTime());
    }

    /**
     * 类路径
     */
    public String getClassPath() {
        return ManagementFactory.getRuntimeMXBean().getClassPath();
    }

    /**
     * 引导类路径
     */
    public String getBootClassPath() {
        return ManagementFactory.getRuntimeMXBean().getBootClassPath();
    }

    /**
     * 线程个数峰值
     */
    public long getPeakThreadCount() {
        return ManagementFactory.getThreadMXBean().getPeakThreadCount();
    }

    /**
     * 活动线程个数
     */
    public long getThreadCount() {
        return ManagementFactory.getThreadMXBean().getThreadCount();
    }

    /**
     * 守护线程个数
     */
    public long getDaemonThreadCount() {
        return ManagementFactory.getThreadMXBean().getDaemonThreadCount();
    }

    /**
     * 获取当前进程的PID
     */
    public int getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        try {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * cpu个数
     */
    public int getAvailableProcessors() {
        return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }

    /**
     * 获取jre供应商
     */
    public String getJREVendor() {
        return System.getProperties().getProperty("java.vm.vendor");
    }

    /**
     * jvm参数
     */
    public String getInputArguments() {
        List<String> argList = ManagementFactory.getRuntimeMXBean().getInputArguments();
        StringBuilder sb = new StringBuilder();
        if (argList != null && !argList.isEmpty()) {
            for (String arg : argList) {
                if (arg == null || arg.trim().length() == 0) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                arg = arg.replaceAll("\\\\", "/");
                sb.append(arg);
            }
        }
        return sb.toString();
    }

    /**
     * 总物理内存
     */
    public long getTotalPhysicalMemorySize() {
        OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osbean.getTotalPhysicalMemorySize();
    }

    /**
     * 分配的虚拟内存
     */
    public long getCommittedVirtualMemorySize() {
        OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osbean.getCommittedVirtualMemorySize();
    }

    /**
     * 交换空间总量
     */
    public long getTotalSwapSpaceSize() {
        OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osbean.getTotalSwapSpaceSize();
    }

    /**
     * 当前类已装入个数
     */
    public long getLoadedClassCount() {
        return ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
    }

    /**
     * 已装入类的总数
     */
    public long getTotalLoadedClassCount() {
        return ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount();
    }

    /**
     * 已卸载类的总数
     */
    public long getUnloadedClassCount() {
        return ManagementFactory.getClassLoadingMXBean().getUnloadedClassCount();
    }

    /**
     * 获取堆内存大小
     */
    public long getHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 获取非堆内存大小
     */
    public long getNonHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 获取最大堆内存大小
     */
    public long getMaxHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 获取最大非堆内存大小
     */
    public long getMaxNonHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 获取初始化堆内存大小
     */
    public long getInitHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 获取初始化非堆内存大小
     */
    public long getInitNonHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getInit();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 获取已提交堆内存大小
     */
    public long getCommittedHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 获取已提交非堆内存大小
     */
    public long getCommittedNonHeapMemoryUsage() {
        try {
            return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getCommitted();
        } catch (Throwable e) {
            //极端情况或JVM内部BUG会导致该处异常或错误
            return 0l;
        }
    }

    /**
     * 工程路径（web）
     * classes路径 （worker）
     */
    public String getApplicationPath() {
        String classPath = null;
        URL classPathURL = null;
        try {
            classPathURL = this.getClass().getClassLoader().getResource("jvm.local");
            classPath = URLDecoder.decode(classPathURL.getPath(), System.getProperty("file.encoding"));
            classPath = classPath.replaceAll("\\\\", "/");
        } catch (Exception e) {
            classPath = this.getClass().getClassLoader().getResource("jvm.local").getPath();
        }
        classPath = classPath.replaceAll("^(file\\:)", "");
        if (classPath.matches(".*(/|\\\\)WEB-INF(/|\\\\)lib(/|\\\\)(.*)$")) {
            return classPath.replaceAll("(/|\\\\)WEB-INF(/|\\\\)lib(/|\\\\)(.*)$", "");
        } else if (classPath.matches(".*(/|\\\\)(([^/\\\\]+)\\.jar\\!(/|\\\\)jvm\\.local)$")) {
            return classPath.replaceAll("(([^/\\\\]+)\\.jar\\!(/|\\\\)jvm\\.local)$", "");
        } else {
            return classPath;
        }
    }

    /**
     * 获取host name
     */
    public String getHostName() {
        if (System.getenv("COMPUTERNAME") != null) {
            return System.getenv("COMPUTERNAME");
        } else {
            try {
                return (InetAddress.getLocalHost()).getHostName();
            } catch (UnknownHostException uhe) {
                String host = uhe.getMessage();
                if (host != null) {
                    int colon = host.indexOf(':');
                    if (colon > 0) {
                        return host.substring(0, colon);
                    }
                }
                return "UnknownHost";
            }
        }
    }

    /**
     * 启动路径
     */
    public String getStartPath() {
        String startPath = System.getProperties().get("user.dir").toString();
        startPath = startPath.replaceAll("\\\\", "/");
        return startPath;
    }

    /**
     * 获取JVM占cpu的百分比
     * <p/>
     * 本百分比值为：
     * 上一次计算结束到现在 的cpu使用率
     * <p/>
     * 注意第一次执行 始终为0.0；
     * 第二次执行才可以计算出第一次到第二次执行之间平均cpu使用率
     */
    public String getCpu() {
        OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long uptimeNow = ManagementFactory.getRuntimeMXBean().getUptime();
        long processCpuTimeNow = osbean.getProcessCpuTime();
        String cpu = "0.0";
        //false:第一次运行;true:上一次调用时间到当前时间，cpu使用率计算
        if (uptime > 0 && processCpuTime > 0) {
            //与上一次调用的时时间差(单位：毫秒)
            long l2 = uptimeNow - uptime;
            //上一次调用到本次调用， cpu被JVM使用的时间(单位：纳秒)
            long l1 = processCpuTimeNow - processCpuTime;
            if (l2 > 0) {
                //result = cpu的耗时 / 时间段的时间差 * (单位换算 / 100.0,这里算出来是10000.0) * cpu个数
                float cpuValue = Math.min(99.0F, (float) l1 / ((float) l2 * 10000.0F * osbean.getAvailableProcessors()));
                //精确到小数点后3位,必须包含一位小数
                DecimalFormat df = new DecimalFormat("##0.0##");
                cpu = df.format(cpuValue);
            }
        }
        uptime = uptimeNow;
        processCpuTime = processCpuTimeNow;
        return cpu;
    }

    @Override
    public String pickJvmEnvironmentInfo() {
        StringBuilder sb = new StringBuilder("{");
        //当前进程的PID
        sb.append(QUOTATION).append("PId").append(QUOTATION).append(COLON).append(QUOTATION).append(getPid()).append(QUOTATION).append(COMMA);
        //jre的版本号
        sb.append(QUOTATION).append("JREV").append(QUOTATION).append(COLON).append(QUOTATION).append(getJREVersion()).append(QUOTATION).append(COMMA);
        //操作系统名称
        sb.append(QUOTATION).append("OSN").append(QUOTATION).append(COLON).append(QUOTATION).append(getOSName()).append(QUOTATION).append(COMMA);
        //操作系统体系
        sb.append(QUOTATION).append("OSA").append(QUOTATION).append(COLON).append(QUOTATION).append(getOSArch()).append(QUOTATION).append(COMMA);
        //处理器个数
        sb.append(QUOTATION).append("OSAP").append(QUOTATION).append(COLON).append(QUOTATION).append(getAvailableProcessors()).append(QUOTATION).append(COMMA);
        //jvm参数
        sb.append(QUOTATION).append("ARGS").append(QUOTATION).append(COLON).append(QUOTATION).append(getInputArguments()).append(QUOTATION).append(COMMA);
        //启动路径
        sb.append(QUOTATION).append("SP").append(QUOTATION).append(COLON).append(QUOTATION).append(getStartPath()).append(QUOTATION).append(COMMA);
        //工程路径
//        sb.append(QUOTATION).append("AP").append(QUOTATION).append(COLON).append(QUOTATION).append(getApplicationPath()).append(QUOTATION).append(COMMA);
        //系统启动时间
        sb.append(QUOTATION).append("ST").append(QUOTATION).append(COLON).append(QUOTATION).append(getStartTime()).append(QUOTATION).append(COMMA);
        //总物理内存
        sb.append(QUOTATION).append("TPMS").append(QUOTATION).append(COLON).append(QUOTATION).append(getTotalPhysicalMemorySize()).append(QUOTATION).append(COMMA);
        //交换空间总量
        sb.append(QUOTATION).append("TSSS").append(QUOTATION).append(COLON).append(QUOTATION).append(getTotalSwapSpaceSize()).append(QUOTATION).append(COMMA);
        //分配的虚拟内存
        sb.append(QUOTATION).append("CVMS").append(QUOTATION).append(COLON).append(QUOTATION).append(getCommittedVirtualMemorySize()).append(QUOTATION).append(COMMA);
        //新生代收集器名称
        sb.append(QUOTATION).append("YGCN").append(QUOTATION).append(COLON).append(QUOTATION).append(getYoungGCName()).append(QUOTATION).append(COMMA);
        //全GC收集器名称
        sb.append(QUOTATION).append("FGCN").append(QUOTATION).append(COLON).append(QUOTATION).append(getFullGCName()).append(QUOTATION);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String pickJvmRumtimeInfo() {
        StringBuilder sb = new StringBuilder("{");
        //线程个数峰值
        sb.append(QUOTATION).append("PTC").append(QUOTATION).append(COLON).append(QUOTATION).append(getPeakThreadCount()).append(QUOTATION).append(COMMA);
        //活动线程个数
        sb.append(QUOTATION).append("TC").append(QUOTATION).append(COLON).append(QUOTATION).append(getThreadCount()).append(QUOTATION).append(COMMA);
        //守护线程个数
        sb.append(QUOTATION).append("DTC").append(QUOTATION).append(COLON).append(QUOTATION).append(getDaemonThreadCount()).append(QUOTATION).append(COMMA);
        //当前类已装入个数
        sb.append(QUOTATION).append("LCC").append(QUOTATION).append(COLON).append(QUOTATION).append(getLoadedClassCount()).append(QUOTATION).append(COMMA);
        //已装入类的总数
        sb.append(QUOTATION).append("TLCC").append(QUOTATION).append(COLON).append(QUOTATION).append(getTotalLoadedClassCount()).append(QUOTATION).append(COMMA);
        //已卸载类的总数
        sb.append(QUOTATION).append("UCC").append(QUOTATION).append(COLON).append(QUOTATION).append(getUnloadedClassCount()).append(QUOTATION).append(COMMA);

        //获取非堆内存大小
        sb.append(QUOTATION).append("NHMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getNonHeapMemoryUsage()).append(QUOTATION).append(COMMA);
        //获取堆内存大小
        sb.append(QUOTATION).append("HMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getHeapMemoryUsage()).append(QUOTATION).append(COMMA);

        //获取初始化非堆内存大小
        sb.append(QUOTATION).append("INHMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getInitNonHeapMemoryUsage()).append(QUOTATION).append(COMMA);
        //获取初始化堆内存大小
        sb.append(QUOTATION).append("IHMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getInitHeapMemoryUsage()).append(QUOTATION).append(COMMA);

        //获取已提交非堆内存大小
        sb.append(QUOTATION).append("CNHMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getCommittedNonHeapMemoryUsage()).append(QUOTATION).append(COMMA);
        //获取已提交堆内存大小
        sb.append(QUOTATION).append("CHMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getCommittedHeapMemoryUsage()).append(QUOTATION).append(COMMA);

        //获取最大非堆内存大小
        sb.append(QUOTATION).append("MNHMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getMaxNonHeapMemoryUsage()).append(QUOTATION).append(COMMA);
        //获取最大堆内存大小
        sb.append(QUOTATION).append("MHMU").append(QUOTATION).append(COLON).append(QUOTATION).append(getMaxHeapMemoryUsage()).append(QUOTATION).append(COMMA);

        //GC
        sb.append(QUOTATION).append("FGCC").append(QUOTATION).append(COLON).append(QUOTATION).append(getFullGCCount()).append(QUOTATION).append(COMMA);
        sb.append(QUOTATION).append("YGCC").append(QUOTATION).append(COLON).append(QUOTATION).append(getYoungGCCount()).append(QUOTATION).append(COMMA);

        sb.append(QUOTATION).append("FGCT").append(QUOTATION).append(COLON).append(QUOTATION).append(getFullGCTime()).append(QUOTATION).append(COMMA);
        sb.append(QUOTATION).append("YGCT").append(QUOTATION).append(COLON).append(QUOTATION).append(getYoungGCTime()).append(QUOTATION).append(COMMA);

        if(getFullGCStartTime()!=null){
            sb.append(QUOTATION).append("FGCS").append(QUOTATION).append(COLON).append(QUOTATION).append(getFullGCStartTime()).append(QUOTATION).append(COMMA);
            sb.append(QUOTATION).append("YGCS").append(QUOTATION).append(COLON).append(QUOTATION).append(getYoungGCStartTime()).append(QUOTATION).append(COMMA);

            sb.append(QUOTATION).append("FGCE").append(QUOTATION).append(COLON).append(QUOTATION).append(getFullGCEndTime()).append(QUOTATION).append(COMMA);
            sb.append(QUOTATION).append("YGCE").append(QUOTATION).append(COLON).append(QUOTATION).append(getYoungGCEndTime()).append(QUOTATION).append(COMMA);

        }

        //JVM占cpu的百分比
        sb.append(QUOTATION).append("CPU").append(QUOTATION).append(COLON).append(QUOTATION).append(getCpu()).append(QUOTATION);
        return sb.append("}").toString();
    }

    @Override
    public String getJvmInstanceCode() {
        int instanceValue = 0;
        String instanceCode = "0";
        try {
        	//修改实例ID的生成规则，取当前启动中间件的相关内容，默认是log，如果没有，则取个性化设置的：server.id
            instanceValue = (getHostName() + getStartPath() + getApplicationPath() + getServerId()).hashCode();
            if (instanceValue < 0) {
                instanceValue = Math.abs(instanceValue);
                instanceCode = String.valueOf(instanceValue);
                instanceCode = getStrWith(instanceCode, String.valueOf(Integer.MIN_VALUE).length());
            } else {
                instanceCode = String.valueOf(instanceValue);
            }
        } catch (Exception e) {
            //DO NOTHING
        }
        return instanceCode;
    }

    private String getStrWith(String str, int len) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < len) {
            if (sb.length() + 1 == len) {
                sb.insert(0, 1);
            } else {
                sb.insert(0, 0);
            }
        }
        return sb.toString();
    }

//	public static void main(String[] args) {
//		System.out.println(LocalJvmInfoPicker.instance.getApplicationPath());
//	}

}