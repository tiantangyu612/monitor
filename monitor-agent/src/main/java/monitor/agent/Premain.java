package monitor.agent;

import monitor.agent.log.AgentLoggerFactory;
import monitor.agent.util.MonitorJarVersionUtil;

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
public class Premain {
    private static Logger LOG = AgentLoggerFactory.getLogger(Premain.class);

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
     * 监控 core jar 名称前缀
     */
    public static String MONITOR_CORE_PREFIX = "monitor-core-";
    /**
     * 监控 core jar 名称后缀
     */
    public static String MONITOR_CORE_SUFFIX = ".jar";


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
            LOG.log(Level.SEVERE, "Premain init error, cause: ", e);
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
        String cluster = monitorConfigProperties.getProperty("cluster");

        FileHandler logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, application, cluster);
        LOG.addHandler(logFileHandler);

        // 获取监控采集器 jar
        // 监控收集器 jar path，需要在类路径下
        String monitorCollectorJarPath = Premain.class.getClassLoader().getResource(".").getPath();
        if (null == monitorCollectorJarPath) {
            LOG.severe("monitor collector jar path is null!");
            return;
        }

        LOG.info("monitor collector jar path is: " + monitorCollectorJarPath);

        String monitorCollectorJar = getMonitorCollectorJar(monitorCollectorJarPath);
        JarFile monitorCollectorJarFile = new JarFile(monitorCollectorJar);
        instrumentation.appendToBootstrapClassLoaderSearch(monitorCollectorJarFile);


        // 初始化 monitor 采集器
        initMonitor(monitorConfigProperties, agentArgs, instrumentation, agentJarPath, logFileHandler);
    }

    /**
     * 验证 agent jar path 是否符合规则，若合法返回 agent jar path
     *
     * @param agentArgs
     * @return
     */
    private static String validateAndGetAgentJarPath(String agentArgs) {
        String agentJarPath = Premain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File agentJarFile = new File(agentJarPath);
        monitorFolder = agentJarFile.getParentFile();

        if (!monitorFolder.getName().equals(MONITOR_FOLDER_NAME)) {
            FileHandler logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, null, null);
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

            logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, application, instance);
            LOG.addHandler(logFileHandler);
            if (application == null) {
                LOG.severe("application property can not be null!");
                return null;
            }

            monitorConfigProperties.setProperty("instance", instance);
            return monitorConfigProperties;
        } catch (FileNotFoundException e) {
            logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);

            String errorMsg = "can not find monitor.properties in: " + monitorFolder.getPath();
            LOG.log(Level.SEVERE, errorMsg, e);
            return null;
        } catch (IOException e) {
            logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);

            String errorMsg = "IOException while reading monitor.properties!";
            LOG.log(Level.SEVERE, errorMsg, e);
            return null;
        }
    }

    /**
     * 获取监控 core jar file
     *
     * @param monitorCollectorJarPath
     * @return
     * @throws IOException
     */
    private static String getMonitorCollectorJar(String monitorCollectorJarPath) throws IOException {
        List<String> monitorCollectorJarList = searchMonitorCoreJars(new File(monitorCollectorJarPath));
        if (monitorCollectorJarList.isEmpty()) {
            LOG.severe("can not found monitor collector jar file named with " + MONITOR_CORE_PREFIX + ".{version}.jar in path: " + monitorCollectorJarPath);
            return null;
        }

        // 获取最高版本的 monitor 采集器 jar
        String highestMonitorCollectorJar = MonitorJarVersionUtil.getHighestMonitorCoreJar(monitorCollectorJarList);
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
     * 查找 monitor core jar
     *
     * @param monitorCoreJarPath
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
            LOG.severe("monitor jar path folder is empty: " + monitorCoreJarPath);
            return Collections.emptyList();
        }

        List<String> monitorCollectorJars = new ArrayList<String>();
        for (File file : files) {
            String fileName = file.getName();
            if ((file.isFile()) && (fileName.startsWith(MONITOR_CORE_PREFIX)) && (fileName.endsWith(MONITOR_CORE_SUFFIX))) {
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
     * @param logFileHandler
     * @throws Exception
     */
    private static void initMonitor(Properties monitorConfigProperties, String agentArgs, Instrumentation instrumentation, String agentJarPath,
                                    FileHandler logFileHandler) throws Exception {
        Class<?> monitorInitializerClass = null;
        try {
            monitorInitializerClass = Class.forName("monitor.core.MonitorInitializer");
        } catch (ClassNotFoundException e) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                classLoader.loadClass("monitor.core.MonitorInitializer");
            } catch (Exception e1) {
                LOG.log(Level.SEVERE, "class not found: monitor.core.MonitorInitializer", e);
                return;
            }
        }

        Object monitorInitializer = monitorInitializerClass.newInstance();

        Class<?>[] monitorInitArgs = {Map.class, Properties.class, Instrumentation.class};
        Method monitorInitMethod = monitorInitializer.getClass().getMethod("initMonitor", monitorInitArgs);

        Map<String, Object> environment = new HashMap<String, Object>();
        environment.put("monitorFolder", monitorFolder.getPath());
        environment.put("agentArgs", agentArgs);
        environment.put("agentJarPath", agentJarPath);
        environment.put("logFileHandler", logFileHandler);

        // 反射执行 collector 初始化方法
        monitorInitMethod.invoke(monitorInitializer, environment, monitorConfigProperties, instrumentation);
        LOG.info("monitor initialized successfully!");
    }
}
