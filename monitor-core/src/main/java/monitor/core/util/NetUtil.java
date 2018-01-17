package monitor.core.util;

import monitor.core.log.MonitorLogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by bjlizhitao on 2016/8/6.
 * 网络操作工具类
 */
public class NetUtil {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(NetUtil.class);

    /**
     * localhost ip 地址
     */
    public static final String LOCALHOST = "127.0.0.1";
    public static final String ANYHOST = "0.0.0.0";
    /**
     * ip 正则表达式
     */
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static volatile InetAddress LOCAL_ADDRESS = null;

    private NetUtil() {
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getIp() {
        InetAddress address = getLocalAddress();
        return address == null ? LOCALHOST : address.getHostAddress();
    }

    /**
     * 遍历本地网卡，返回第一个合理的IP。
     *
     * @return 本地网卡IP
     */
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null)
            return LOCAL_ADDRESS;
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    /**
     * 扫描各个网卡获取合理的ip地址
     *
     * @return
     */
    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            LOGGER.log(Level.INFO, "Failed to retriving ip address, " + e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();// 获取网络接口集合
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();

                        Enumeration<InetAddress> addresses = network.getInetAddresses();

                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    LOGGER.log(Level.INFO, "Failed to retriving ip address, " + e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        LOGGER.log(Level.INFO, "Failed to retriving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            LOGGER.log(Level.INFO, "Failed to retriving ip address, " + e.getMessage(), e);
        }
        LOGGER.log(Level.SEVERE, "Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    /**
     * 是否为合理的 ip 地址
     *
     * @param address
     * @return
     */
    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null
                && !ANYHOST.equals(name)
                && !LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }
}
