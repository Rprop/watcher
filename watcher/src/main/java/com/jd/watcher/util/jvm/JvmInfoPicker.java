package com.jd.watcher.util.jvm;

/**
 * JVM信息获取接口
 *
 * @author chenhualiang
 * @since 2013-01-28
 */
public interface JvmInfoPicker {

    /**
     * 采集JVM运行环境信息
     * <p/>
     * <p>
     * 采集数据为一个json串，格式信息见下例：
     * <pre>
     * {"PId":"1264","JREV":"1.6.0_25","args":"-Dfile.encoding=GBK","TPMS":"3845201920",
     * "SP":"E:\360buy\projects\jvm-watcher","AP":"/E:/360buy/projects/jvm-watcher/WebContent",
     * "TSSS":"4294967295","CVMS":"42496000"}
     * **************************************
     * PId：	process Id    进程ID
     * JREV：JRE Version	JRE版本
     * args：Input Arguments	JVM启动参数
     * TPMS：Total Physical Memory Size	物理内存大小（字节）
     * SP：Start Path	启动路径
     * AP：Application Path	应用路径
     * TSSS：Total Swap Space Size	交换区大小（字节）
     * CVMS：Committed Virtual Memory Size	虚拟内存大小（字节）
     * *************************************
     * </pre>
     * </p>
     *
     * @return 采集数据
     * @author chenhualiang
     * @since 2013-01-28
     */
    public String pickJvmEnvironmentInfo();

    /**
     * 采集JVM运行时信息
     * <p/>
     * <p>
     * 采集数据为一个json串，格式信息见下例：
     * <pre>
     * {"PTC":"5","TC":"5","DTC":"4","LCC":"417","TLCC":"417","UCC":"0","NHMU":"13728272","HMU":"510024","CPU":"1%"}
     * **************************************
     * PTC：Peak Thread Count	线程峰值
     * TC：Thread Count	当前线程数
     * DTC：Daemon Thread Count	守保线程数
     * LCC：Loaded Class Count	加载类数
     * TLCC：Total Loaded Class Count	加载类总数
     * UCC：Unloaded Class Count	卸载类总数
     * NHMU：Non Heap Memory Usage	非堆栈内存使用量（字节）
     * HMU：Heap Memory Usage	堆栈内存使用量（字节）
     * CPU：CPU	JVM cpu占用率（浮点型）
     * *************************************
     * </pre>
     * </p>
     *
     * @return 采集数据
     * @author chenhualiang
     * @since 2013-01-28
     */
    public String pickJvmRumtimeInfo();

    /**
     * 获取JVM的实例的编码
     * <p/>
     * <p>
     * 编码生成原理：
     * （启动路径 + 工程路径）字符串的hash值
     * </p>
     *
     * @return JVM的实例的编码
     * @since 2013-01-28
     */
    public String getJvmInstanceCode();

}
