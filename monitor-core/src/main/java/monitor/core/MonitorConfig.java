package monitor.core;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/6.
 * 监控配置信息
 */
public class MonitorConfig {
    private static Logger LOG = MonitorLogFactory.getLogger(MonitorConfig.class);

    /**
     * 监控环境信息
     */
    private static MonitorEnv monitorEnv;

    /**
     * 应用名称
     */
    private static String application;
    /**
     * 集群名称
     */
    private static String cluster;

    /**
     * 是否启用 java method 数据采集
     */
    private static boolean isEnableJavaMethodCollect = true;
    /**
     * 采集的java method 的最大数量，默认 2000 个方法，最大可设置到 5000 个，限制数量防止内存溢出
     */
    private static int maxCollectJavaMethodCount = 2000;
    /**
     * 采集的java method 的最大限制值
     */
    private static final int MAX_COLLECT_JAVA_METHOD_COUNT_LIMIT = 5000;

    /**
     * 初始化监控配置
     *
     * @param monitorEnv
     * @param monitorConfigProperties
     * @return
     */
    public static void initConfig(MonitorEnv monitorEnv, Properties monitorConfigProperties) {
        // 设置监控环境信息
        MonitorConfig.monitorEnv = monitorEnv;

        // 获取监控配置信息
        Properties monitorConfig = getMonitorConfig(monitorConfigProperties);
        // 设置监控配置信息
        setMonitorConfig(monitorConfig);
    }

    /**
     * 获取监控配置
     *
     * @param monitorConfigProperties
     * @return
     */
    private static Properties getMonitorConfig(Properties monitorConfigProperties) {
        Properties monitorConfig = new Properties();

        Set<Map.Entry<Object, Object>> configEntrySet = monitorConfigProperties.entrySet();
        for (Map.Entry<Object, Object> configEnrty : configEntrySet) {
            String key = (String) configEnrty.getKey();
            String value = (String) configEnrty.getValue();
            monitorConfig.setProperty(key.trim(), value.trim());
        }

        return monitorConfig;
    }

    /**
     * 设置监控配置
     *
     * @param monitorConfig
     */
    private static void setMonitorConfig(Properties monitorConfig) {
        setMonitorBasicConfig(monitorConfig);
        setJavaMethodCollectConfig(monitorConfig);
    }

    /**
     * 设置基本配置
     *
     * @param monitorConfig
     */
    private static void setMonitorBasicConfig(Properties monitorConfig) {
        MonitorConfig.application = monitorConfig.getProperty("application");
        MonitorConfig.cluster = monitorConfig.getProperty("cluster");
    }

    /**
     * 设置 java method collect 配置
     *
     * @param monitorConfig
     */
    private static void setJavaMethodCollectConfig(Properties monitorConfig) {
        // 是否启用 java method 采集
        String isEnableJavaMethodCollectValue = monitorConfig.getProperty("isEnableJavaMethodCollect");
        if ("false".equals(isEnableJavaMethodCollectValue)) {
            MonitorConfig.isEnableJavaMethodCollect = false;
        }

        // java method 采集最大数量
        String maxCollectJavaMethodCountValue = monitorConfig.getProperty("maxCollectJavaMethodCount");
        if (!"null".equals(maxCollectJavaMethodCountValue)) {
            try {
                MonitorConfig.maxCollectJavaMethodCount = Integer.parseInt(maxCollectJavaMethodCountValue);
                if (MonitorConfig.maxCollectJavaMethodCount > MonitorConfig.MAX_COLLECT_JAVA_METHOD_COUNT_LIMIT) {
                    MonitorConfig.maxCollectJavaMethodCount = MonitorConfig.MAX_COLLECT_JAVA_METHOD_COUNT_LIMIT;
                }
            } catch (Exception e) {
                // NOP
            }
        }
    }

    /**
     * 获取监控环境信息
     *
     * @return
     */
    public static MonitorEnv getMonitorEnv() {
        return MonitorConfig.monitorEnv;
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getApplication() {
        return MonitorConfig.application;
    }

    /**
     * 获取集群名称
     *
     * @return
     */
    public static String getCluster() {
        return MonitorConfig.cluster;
    }

    /**
     * 获取是否启用 java method 数据采集
     *
     * @return
     */
    public static boolean isEnableJavaMethodCollect() {
        return MonitorConfig.isEnableJavaMethodCollect;
    }

    /**
     * 获取采集的java method 的最大数量
     *
     * @return
     */
    public static int getMaxCollectJavaMethodCount() {
        return MonitorConfig.maxCollectJavaMethodCount;
    }
}
