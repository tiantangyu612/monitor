package monitor.agent.util;

import monitor.agent.exception.MonitorJarNotFoundException;
import monitor.agent.exception.PropertyNotFoundException;
import monitor.agent.log.AgentLoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/4.
 * monitor core jar util，监控 core jar 版本工具类
 * monitor-core jar 命名类似 monitor-core-0.2.1.jar，必须为该规则
 */
public class MonitorCoreJarUtil {
    private static Logger LOG = AgentLoggerFactory.getLogger(MonitorCoreJarUtil.class);

    /**
     * 获取最高版的监控 core jar
     *
     * @return String
     * @throws IOException
     */
    public static String getMonitorCoreJarPath() throws Exception {
        Object monitorCoreJarPath = System.getProperties().get(Constants.MONITOR_CORE_LIB_PATH_PROPERTY);
        if (monitorCoreJarPath == null) {
            String errorMsg = Constants.MONITOR_CORE_LIB_PATH_PROPERTY + " property not found in system property";
            LOG.log(Level.SEVERE, errorMsg);
            throw new PropertyNotFoundException(errorMsg);
        }

        String monitorCoreJar = getMonitorCoreJar(monitorCoreJarPath.toString());
        if (monitorCoreJar == null) {
            String errorMsg = monitorCoreJarPath + " can not be found";
            LOG.log(Level.SEVERE, errorMsg);
            throw new MonitorJarNotFoundException(errorMsg);
        }

        return monitorCoreJar;
    }

    /**
     * 获取监控 core jar file
     *
     * @param monitorCoreJarPath monitor-core-{version}.jar 文件路径
     * @return
     * @throws IOException
     */
    private static String getMonitorCoreJar(String monitorCoreJarPath) throws IOException {
        List<String> monitorCoreJarList = searchMonitorCoreJars(new File(monitorCoreJarPath));
        if (monitorCoreJarList.isEmpty()) {
            LOG.severe("can not found monitor core jar file named with " + Constants.MONITOR_CORE_PREFIX + ".{version}.jar in path: " + monitorCoreJarPath);
            return null;
        }

        // 获取最高版本的 monitor core jar
        String highestMonitorCoreJar = MonitorCoreJarUtil.getHighestMonitorCoreJar(monitorCoreJarList);
        LOG.info("use highest monitor core jar version: " + highestMonitorCoreJar);

        File highestMonitorCoreJarFile = new File(highestMonitorCoreJar);
        if (!highestMonitorCoreJarFile.exists()) {
            LOG.severe("highest monitor core jar file not exist: " + highestMonitorCoreJar);
            return null;
        }
        if (!highestMonitorCoreJarFile.isFile()) {
            LOG.severe("highest monitor core jar file not a file: " + highestMonitorCoreJar);
            return null;
        }

        return highestMonitorCoreJar;
    }

    /**
     * 查找 monitor core jar
     *
     * @param monitorCoreJarPath monitor-core-{version}.jar 文件路径
     * @return
     */
    private static List<String> searchMonitorCoreJars(File monitorCoreJarPath) {
        if (!monitorCoreJarPath.exists()) {
            LOG.severe("monitor core jar path not found: " + monitorCoreJarPath);
            return Collections.emptyList();
        }

        if (!monitorCoreJarPath.isDirectory()) {
            LOG.severe("monitor core jar path is not a directory: " + monitorCoreJarPath);
            return Collections.emptyList();
        }

        File[] files = monitorCoreJarPath.listFiles();
        if ((files == null) || (files.length == 0)) {
            LOG.severe("monitor core jar path folder is empty: " + monitorCoreJarPath);
            return Collections.emptyList();
        }

        List<String> monitorCoreJars = new ArrayList<String>();
        for (File file : files) {
            String fileName = file.getName();
            if ((file.isFile()) && (fileName.startsWith(Constants.MONITOR_CORE_PREFIX)) && (fileName.endsWith(Constants.MONITOR_CORE_SUFFIX))) {
                monitorCoreJars.add(file.getPath());
            }
        }

        return monitorCoreJars;
    }

    /**
     * 获取最高版本的 monitor-core jar 版本号
     *
     * @param monitorCoreJars 监控 core jar 列表
     * @return
     */
    private static String getHighestMonitorCoreJar(List<String> monitorCoreJars) {
        String highestCollectorJar = null;
        for (String collectorJar : monitorCoreJars) {
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
     * @param jar        jar 名称
     * @param anotherJar 另一个 jar 名称
     * @return
     */
    private static boolean isHigherVersionJar(String jar, String anotherJar) {
        int monitorCollectorPrefixLength = Constants.MONITOR_CORE_PREFIX.length();
        int monitorCollectorSuffixLength = Constants.MONITOR_CORE_SUFFIX.length();

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
     * @param jarVersion jar 版本
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
}