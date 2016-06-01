package com.whh.watcher.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 跟机器相关的工具类
 */
public class HostUtil {
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    /**
     * 获取本地的ip地址
     *
     * @return ip地址
     */
    public static final String getLocalIp() {
        InetAddress localAddress = getLocalAddress();
        if (localAddress != null) {
            return localAddress.getHostAddress();
        } else {
            return "127.0.0.1";
        }
    }

    /**
     * 获取本地的机器名称
     *
     * @return ip地址
     */
    public static final String getLocalHostName() {
        InetAddress localAddress = getLocalAddress();
        if (localAddress != null) {
            return localAddress.getHostName();
        } else {
            return "localhost";
        }
    }

    /**
     * 将IP地址转为16进制的string
     * @param ip ip地址
     * @return
     */
    public static String ipToHexStr(String ip) {
        String[] ips = ip.split("\\.");
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) Integer.parseInt(ips[i]);
        }
        StringBuilder sb = new StringBuilder(bytes.length / 2);
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b >> 4) & 0x0F));
            sb.append(Integer.toHexString(b & 0x0F));
        }
        return sb.toString();
    }

    /**
     * 将16进制的String 转为IP地址
     * @param ip 16进制IP地址
     * @return
     */
    public static String hexStrToIP(String ip) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < 8; i = i + 2) {
            sb.append(".").append(Integer.valueOf(ip.substring(i, i + 2), 16));
        }
        return sb.substring(1).toString();
    }

    //将10进制整数形式转换成127.0.0.1形式的ip地址，在命令提示符下输入ping3396362403l
    public static String longToIp(long longIP) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf(longIP >>> 24));//直接右移24位
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x00ffffff) >>> 16));//将高8位置0，然后右移16位
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x0000ffff) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longIP & 0x000000ff));
        return sb.toString();
    }


    private static InetAddress getLocalAddress() {
        InetAddress localAddress = null;
        try {
            // 如果能直接取到正确IP就返回，通常windows下可以
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {

        }
        try {
            // 通过轮询网卡接口来获取IP
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null)
                while (interfaces.hasMoreElements())
                    try {
                        NetworkInterface network = (NetworkInterface) interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = (InetAddress) addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {

                                }
                            }
                        }
                    } catch (Throwable e) {
                    }
        } catch (Throwable e) {
        }
        return localAddress;
    }

    /**
     * 判断是否为有效合法的外部IP，而非内部回环IP
     *
     * @param address
     * @return
     */
    private static boolean isValidAddress(InetAddress address) {
        if ((address == null) || (address.isLoopbackAddress())) {
            return false;
        }

        String ip = address.getHostAddress();

        return (ip != null) && (!"0.0.0.0".equals(ip)) && (!"127.0.0.1".equals(ip)) && (IP_PATTERN.matcher(ip).matches());
    }

    public static void main(String[] args) {
        String ip = HostUtil.getLocalIp();
        System.out.println(ip);
        String hexStr = HostUtil.ipToHexStr(ip);
        System.out.println(hexStr);
        System.out.println(HostUtil.hexStrToIP(hexStr));

    }
}
