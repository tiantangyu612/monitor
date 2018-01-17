package monitor.core.config;

import monitor.core.MonitorEnv;
import monitor.core.log.MonitorLogFactory;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/6.
 * 监控配置信息
 */
public class MonitorConfig {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(MonitorConfig.class);

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
     * 是否启用 jvm 相关数据采集
     */
    private static boolean enableJVMInfoCollect = true;

    /**
     * 是否启用 java method 数据采集
     */
    private static boolean enableJavaMethodCollect = true;
    /**
     * 采集的java method 的最大数量，默认 2000 个方法，最大可设置到 5000 个，限制数量防止内存溢出
     */
    private static int maxCollectJavaMethodCount = 2000;
    /**
     * 采集的java method 的最大限制值
     */
    private static final int MAX_COLLECT_JAVA_METHOD_COUNT_LIMIT = 5000;
    /**
     * 是否启用 tomcat 数据采集
     */
    private static boolean enableTomcatCollect = true;
    /**
     * 采集数据上报处理类工厂
     */
    private static String monitorReporterFactory = "monitor.core.report.MonitorSocketReporterFactory";
    /**
     * 数据上报中心 url，用于采集数据上报
     */
    private static String dataHubUrl = "127.0.0.1:16666";

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
        setJVMInfoCollectConfig(monitorConfig);
        setJavaMethodCollectConfig(monitorConfig);
        setTomcatCollectConfig(monitorConfig);
    }

    /**
     * 设置基本配置
     *
     * @param monitorConfig
     */
    private static void setMonitorBasicConfig(Properties monitorConfig) {
        MonitorConfig.application = monitorConfig.getProperty("application");
        MonitorConfig.cluster = monitorConfig.getProperty("cluster");
        setMonitorReporterFactory(monitorConfig);
        setDataHubUrl(monitorConfig);
    }

    /**
     * 设置 jvm info collect 配置
     *
     * @param monitorConfig
     */
    private static void setJVMInfoCollectConfig(Properties monitorConfig) {
        // 是否启用 jvm 信息采集
        String enableJVMInfoCollectValue = monitorConfig.getProperty("enableJVMInfoCollect");
        if ("false".equals(enableJVMInfoCollectValue)) {
            MonitorConfig.enableJVMInfoCollect = false;
        }
    }

    /**
     * 设置 java method collect 配置
     *
     * @param monitorConfig
     */
    private static void setJavaMethodCollectConfig(Properties monitorConfig) {
        // 是否启用 java method 采集
        String enableJavaMethodCollectValue = monitorConfig.getProperty("enableJavaMethodCollect");
        if ("false".equals(enableJavaMethodCollectValue)) {
            MonitorConfig.enableJavaMethodCollect = false;
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
     * 设置 tomcat collect 配置
     *
     * @param monitorConfig
     */
    private static void setTomcatCollectConfig(Properties monitorConfig) {
        // 是否启用 tomcat 信息采集
        String enableTomcatCollectValue = monitorConfig.getProperty("enableTomcatCollect");
        if ("false".equals(enableTomcatCollectValue)) {
            MonitorConfig.enableTomcatCollect = false;
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
     * 获取是否启用 jvm 相关数据采集
     *
     * @return
     */
    public static boolean isEnableJVMInfoCollect() {
        return MonitorConfig.enableJVMInfoCollect;
    }

    /**
     * 获取是否启用 java method 数据采集
     *
     * @return
     */
    public static boolean isEnableJavaMethodCollect() {
        return MonitorConfig.enableJavaMethodCollect;
    }

    /**
     * 获取采集的java method 的最大数量
     *
     * @return
     */
    public static int getMaxCollectJavaMethodCount() {
        return MonitorConfig.maxCollectJavaMethodCount;
    }

    /**
     * 获取是否启用 tomcat 数据采集
     *
     * @return
     */
    public static boolean isEnableTomcatCollect() {
        return enableTomcatCollect;
    }

    /**
     * 获取采集数据上报处理类工厂
     *
     * @return
     */
    public static String getMonitorReporterFactory() {
        return monitorReporterFactory;
    }

    /**
     * 设置采集数据上报处理类工厂
     *
     * @param monitorConfig
     */
    public static void setMonitorReporterFactory(Properties monitorConfig) {
        if (null != monitorConfig.get("monitorReporterFactory")) {
            MonitorConfig.monitorReporterFactory = monitorConfig.getProperty("monitorReporterFactory");
        }
    }

    /**
     * 获取数据上报中心 url，用于采集数据上报
     *
     * @return
     */
    public static String getDataHubUrl() {
        return dataHubUrl;
    }

    /**
     * 设置数据上报中心 url，用于采集数据上报
     *
     * @param monitorConfig
     */
    public static void setDataHubUrl(Properties monitorConfig) {
        if (null != monitorConfig.get("dataHubUrl")) {
            MonitorConfig.dataHubUrl = monitorConfig.getProperty("dataHubUrl");
        }
    }
}
