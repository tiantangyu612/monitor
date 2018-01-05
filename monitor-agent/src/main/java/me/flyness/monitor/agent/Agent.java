package me.flyness.monitor.agent;

import me.flyness.monitor.agent.log.MonitorLoggerFactory;
import me.flyness.monitor.agent.util.MonitorJarVersionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/4.
 * java agent 用于增加方法监控，该类实现 premain 方法
 */
public class Agent {
    private static Logger LOG = MonitorLoggerFactory.getLogger(Agent.class);

    /**
     * 监控 agent jar 所在文件夹名称
     */
    private static File monitorFolder = null;
    /**
     * 监控 agent jar 所在文件夹名称常量，文件夹名称必须为该名称
     */
    public static final String MONITOR_FOLDER_NAME = "monitor";
    /**
     * 监控配置文件名称
     */
    public static String CONFIG_FILE_NAME = "monitor.properties";
    /**
     * 监控采集器 jar 名称前缀
     */
    public static String MONITOR_COLLECTOR_PREFIX = "monitor-collector-";
    /**
     * 监控采集器 jar 名称前缀
     */
    public static String MONITOR_COLLECTOR_SUFFIX = ".jar";

    /**
     * 该方法在 main 方法执行前调用
     *
     * @param agentArgs
     * @param instrumentation
     * @throws Exception
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
        try {
            initAgent(agentArgs, instrumentation);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Agent init error, cause: ", e);
        }
    }

    /**
     * 初始化 agent
     *
     * @param agentArgs
     * @param instrumentation
     * @throws Exception
     */
    private static void initAgent(String agentArgs, Instrumentation instrumentation) throws Exception {
        // 获取 agent jar path，agent jar 必须在类路径下的 monitor-agent 文件夹下
        String agentJarPath = validateAndGetAgentJarPath(agentArgs);
        if (null == agentJarPath) {
            return;
        }

        // 加载监控配置文件，文件名称必须为 monitor.properties
        Properties monitorConfigProperties = loadMonitorConfigProperties(agentArgs);
        String application = monitorConfigProperties.getProperty("application");
        String instance = monitorConfigProperties.getProperty("instance");

        FileHandler logFileHandler = MonitorLoggerFactory.buildLogFileHandler(agentArgs, application, instance);
        LOG.addHandler(logFileHandler);

        // 监控收集器 jar path，需要在类路径下
        String monitorCollectorJarPath = Agent.class.getClassLoader().getResource(".").getPath();
        if (null == monitorCollectorJarPath) {
            LOG.severe("monitor collector jar path is null!");
            return;
        }

        LOG.info("monitor collector jar path is: " + monitorCollectorJarPath);

        // 获取监控采集器 jar
        String monitorCollectorJar = getMonitorCollectorJar(monitorCollectorJarPath);
        JarFile monitorCollectorJarFile = new JarFile(monitorCollectorJar);
        instrumentation.appendToBootstrapClassLoaderSearch(monitorCollectorJarFile);

        // 初始化 monitor 采集器
        initMonitorCollector(monitorConfigProperties, agentArgs, instrumentation, agentJarPath,
                monitorCollectorJar, logFileHandler, application, instance);
    }

    /**
     * 验证 agent jar path 是否符合规则，若合法返回 agent jar path
     *
     * @param agentArgs
     * @return
     */
    private static String validateAndGetAgentJarPath(String agentArgs) {
        String agentJarPath = Agent.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File agentJarFile = new File(agentJarPath);
        monitorFolder = agentJarFile.getParentFile();

        if (!monitorFolder.getName().equals(MONITOR_FOLDER_NAME)) {
            FileHandler logFileHandler = MonitorLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);
            LOG.severe("init agent error, cause the folder name must be " + MONITOR_FOLDER_NAME + ", bye bye!");
            return null;
        }

        return agentJarPath;
    }

    /**
     * 加载监控配置
     *
     * @param agentArgs
     * @return
     */
    private static Properties loadMonitorConfigProperties(String agentArgs) {
        File configPropertiesFile = new File(monitorFolder.getPath() + File.separator + CONFIG_FILE_NAME);

        Properties monitorConfigProperties = new Properties();
        FileHandler logFileHandler;

        try {
            monitorConfigProperties.load(new FileInputStream(configPropertiesFile));
            String application = monitorConfigProperties.getProperty("application");

            String instance = monitorConfigProperties.getProperty("instance");
            if (instance == null) {
                instance = "default";
            }

            logFileHandler = MonitorLoggerFactory.buildLogFileHandler(agentArgs, application, instance);
            LOG.addHandler(logFileHandler);
            if (application == null) {
                LOG.severe("application property can not be null!");
                return null;
            }

            monitorConfigProperties.setProperty("instance", instance);
            return monitorConfigProperties;
        } catch (FileNotFoundException e) {
            logFileHandler = MonitorLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);

            String errorMsg = "can not find monitor.properties in: " + monitorFolder.getPath();
            LOG.log(Level.SEVERE, errorMsg, e);
            return null;
        } catch (IOException e) {
            logFileHandler = MonitorLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);

            String errorMsg = "IOException while reading monitor.properties!";
            LOG.log(Level.SEVERE, errorMsg, e);
            return null;
        }
    }

    /**
     * 获取监控采集器 jar file
     *
     * @param monitorCollectorJarPath
     * @return
     * @throws IOException
     */
    private static String getMonitorCollectorJar(String monitorCollectorJarPath) throws IOException {
        List<String> monitorCollectorJarList = searchMonitorCollectorJars(new File(monitorCollectorJarPath));
        if (monitorCollectorJarList.isEmpty()) {
            LOG.severe("can not found monitor collector jar file named with " + MONITOR_COLLECTOR_PREFIX + ".{version}.jar in path: " + monitorCollectorJarPath);
            return null;
        }

        // 获取最高版本的 monitor 采集器 jar
        String highestMonitorCollectorJar = MonitorJarVersionUtil.getHighestMonitorCollectorJar(monitorCollectorJarList);
        LOG.info("use highest monitor collector jar version: " + highestMonitorCollectorJar);

        File highestMonitorCollectorJarFile = new File(highestMonitorCollectorJar);
        if (!highestMonitorCollectorJarFile.exists()) {
            LOG.severe("highest monitor collector jar file not exist: " + highestMonitorCollectorJar);
            return null;
        }
        if (!highestMonitorCollectorJarFile.isFile()) {
            LOG.severe("highest monitor collector jar file not a file: " + highestMonitorCollectorJar);
            return null;
        }

        return highestMonitorCollectorJar;
    }

    /**
     * 查找 monitor 采集器 jar
     *
     * @param monitorCollectorJarPath
     * @return
     */
    private static List<String> searchMonitorCollectorJars(File monitorCollectorJarPath) {
        if (!monitorCollectorJarPath.exists()) {
            LOG.severe("monitor collector jar path not found: " + monitorCollectorJarPath);
            return Collections.emptyList();
        }

        if (!monitorCollectorJarPath.isDirectory()) {
            LOG.severe("monitor collector jar path is not a directory: " + monitorCollectorJarPath);
            return Collections.emptyList();
        }

        File[] files = monitorCollectorJarPath.listFiles();
        if ((files == null) || (files.length == 0)) {
            LOG.severe("monitor jar path folder is empty: " + monitorCollectorJarPath);
            return Collections.emptyList();
        }

        List<String> monitorCollectorJars = new ArrayList<String>();
        for (File file : files) {
            String fileName = file.getName();
            if ((file.isFile()) && (fileName.startsWith(MONITOR_COLLECTOR_PREFIX)) && (fileName.endsWith(MONITOR_COLLECTOR_SUFFIX))) {
                monitorCollectorJars.add(file.getPath());
            }
        }

        return monitorCollectorJars;
    }

    /**
     * 初始化 monitor 采集器
     *
     * @param monitorConfigProperties
     * @param agentArgs
     * @param instrumentation
     * @param agentJarPath
     * @param monitorCollectorJar
     * @param logFileHandler
     * @param application
     * @param instance
     * @throws Exception
     */
    private static void initMonitorCollector(Properties monitorConfigProperties, String agentArgs, Instrumentation instrumentation, String agentJarPath,
                                             String monitorCollectorJar, FileHandler logFileHandler, String application, String instance) throws Exception {
        Class<?> collectorInitializerClass = Class.forName("me.flyness.monitor.collector.CollectorInitializer");
        Object collectorInitializer = collectorInitializerClass.newInstance();

        Class<?>[] collectorInitArgs = {Map.class, Properties.class, Instrumentation.class};
        Method collectorInitMethod = collectorInitializer.getClass().getMethod("initCollector", collectorInitArgs);

        Map<String, Object> environment = new HashMap<String, Object>();
        environment.put("monitorFolder", monitorFolder.getPath());
        environment.put("agentArgs", agentArgs);
        environment.put("agentJarPath", agentJarPath);
        environment.put("collectorPath", monitorCollectorJar);
        environment.put("logFileHandler", logFileHandler);
        environment.put("application", application);
        environment.put("instance", instance);

        // 反射执行 collector 初始化方法
        collectorInitMethod.invoke(collectorInitializer, environment, monitorConfigProperties, instrumentation);
        LOG.info("monitor collector initialized successfully!");
    }
}
