package me.flyness.monitor.agent.util;

import me.flyness.monitor.agent.Agent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/4.
 * monitor jar version util，监控 jar 版本工具类
 */
public class MonitorJarVersionUtil {
    /**
     * 获取最高版本的监控收集器 monitor-collector jar 版本号
     *
     * @param collectorJars
     * @return
     */
    public static String getHighestMonitorCollectorJar(List<String> collectorJars) {
        String highestCollectorJar = null;
        for (String collectorJar : collectorJars) {
            if (highestCollectorJar == null) {
                highestCollectorJar = collectorJar;
            } else if (isHigherVersionJar(collectorJar, highestCollectorJar)) {
                highestCollectorJar = collectorJar;
            }
        }
        return highestCollectorJar;
    }

    /**
     * 是否是更高版本的 jar 版本号
     *
     * @param jar
     * @param anotherJar
     * @return
     */
    private static boolean isHigherVersionJar(String jar, String anotherJar) {
        int monitorCollectorPrefixLength = Agent.MONITOR_COLLECTOR_PREFIX.length();
        int monitorCollectorSuffixLength = Agent.MONITOR_COLLECTOR_SUFFIX.length();

        String jarVersion = jar.substring(monitorCollectorPrefixLength, jar.length() - monitorCollectorSuffixLength);
        String anotherJarVersion = anotherJar.substring(monitorCollectorPrefixLength, anotherJar.length() - monitorCollectorSuffixLength);

        int[] jarVersionNumbers = splitVersion(jarVersion);
        int[] anotherJarVersionNumbers = splitVersion(anotherJarVersion);
        for (int i = 0; i < 3; i++) {
            if (jarVersionNumbers[i] > anotherJarVersionNumbers[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * 分割 jar 版本号
     *
     * @param jarVersion
     * @return
     */
    private static int[] splitVersion(String jarVersion) {
        String[] versions = jarVersion.split("\\.");
        int[] versionNumbers = new int[3];
        versionNumbers[0] = Integer.parseInt(versions[0]);
        versionNumbers[1] = Integer.parseInt(versions[1]);
        versionNumbers[2] = Integer.parseInt(versions[2]);
        return versionNumbers;
    }

    public static void main(String[] args) {
        String jar1 = "monitor-collector-0.2.1.jar";
        String jar2 = "monitor-collector-0.3.4.jar";
        String jar3 = "monitor-collector-1.0.1.jar";
        String jar4 = "monitor-collector-1.2.6.jar";

        List<String> jars = new ArrayList<String>();
        jars.add(jar1);
        jars.add(jar2);
        jars.add(jar3);
        jars.add(jar4);

        System.out.println(getHighestMonitorCollectorJar(jars));
    }
}