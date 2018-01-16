package monitor.agent.util;

/**
 * Created by lizhitao on 2018/1/11.
 * 常量类
 */
public class Constants {
    private Constants() {
    }

    /**
     * java -Dmonitor_core_lib_path=........
     * monitor core lib path
     */
    public static final String MONITOR_CORE_LIB_PATH_PROPERTY = "monitor_core_lib_path";
    /**
     * 监控初始化类
     */
    public static final String MONITOR_INITIALIZER_CLASS = "monitor.core.MonitorInitializer";
    /**
     * 监控初始化方法名
     */
    public static final String MONITOR_INITIALIZER_METHOD = "initMonitor";
    /**
     * 监控 agent jar 所在文件夹名称常量，文件夹名称必须为该名称
     */
    public static final String MONITOR_FOLDER_NAME = "monitor";
    /**
     * 监控配置文件名称
     */
    public static final String CONFIG_FILE_NAME = "monitor.properties";
    /**
     * 监控 core jar 名称前缀
     */
    public static final String MONITOR_CORE_PREFIX = "monitor-core-";
    /**
     * 监控 core jar 名称后缀
     */
    public static final String MONITOR_CORE_SUFFIX = ".jar";
    /**
     * 默认 cluster 名称
     */
    public static final String DEFAULT_CLUSTER_NAME = "default";
}
