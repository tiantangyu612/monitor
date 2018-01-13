package monitor.agent;

import monitor.agent.config.MonitorConfigProperties;
import monitor.agent.exception.MonitorJarNotFoundException;
import monitor.agent.log.AgentLoggerFactory;
import monitor.agent.util.Constants;
import monitor.agent.util.MonitorCoreJarUtil;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/11.
 * monitor agent
 */
public class MonitorAgent {
    private static Logger LOG = AgentLoggerFactory.getLogger(MonitorAgent.class);

    /**
     * 监控 agent jar 所在文件夹名称，该文件夹名称必须为 monitor
     */
    private static File monitorFolder = null;

    /**
     * 初始化 agent
     *
     * @param agentArgs       传入的 agent 参数
     * @param instrumentation Instrumentation 对象
     * @throws Exception
     */
    public static void init(String agentArgs, Instrumentation instrumentation) throws Exception {
        // 获取 monitor agent jar path，monitor agent jar 必须在类路径下的 monitor 文件夹下
        String agentJarPath = validateAndGetAgentJarPath(agentArgs);

        // 加载监控配置文件，文件名称必须为 monitor.properties
        Properties monitorConfigProperties = MonitorConfigProperties.loadProperties(agentArgs, monitorFolder);

        // 获取 logFileHandler
        FileHandler logFileHandler = initAndGetLogFileHandler(agentArgs, monitorConfigProperties);

        // 获取最高版的监控 core jar，并添加到 BootstrapClassLoaderSearch
        // 将该 jar 包添加到 Bootstrap Classloader Search 用于解决 tomcat 特殊的类加载机制加载不到该 jar 包中的类
        String monitorCoreJarPath = MonitorCoreJarUtil.getMonitorCoreJarPath();
        JarFile monitorCoreJarFile = new JarFile(monitorCoreJarPath);
        instrumentation.appendToBootstrapClassLoaderSearch(monitorCoreJarFile);

        // 执行初始化 monitor 操作
        doInitMonitor(monitorConfigProperties, agentArgs, instrumentation, agentJarPath, logFileHandler, monitorCoreJarPath);
    }

    /**
     * 初始化 FileHandler 并返回
     *
     * @param agentArgs               传入的 agent 参数
     * @param monitorConfigProperties 监控配置属性
     * @return FileHandler
     */
    private static FileHandler initAndGetLogFileHandler(String agentArgs, Properties monitorConfigProperties) {
        // 初始化日志配置
        String application = monitorConfigProperties.getProperty("application");
        String cluster = monitorConfigProperties.getProperty("cluster");
        FileHandler logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, application, cluster);
        LOG.addHandler(logFileHandler);

        return logFileHandler;
    }

    /**
     * 验证 agent jar path 是否符合规则，若合法返回 agent jar path
     *
     * @param agentArgs 传入的 agent 参数
     * @return String，agent jar 路径
     */
    private static String validateAndGetAgentJarPath(String agentArgs) throws MonitorJarNotFoundException {
        String agentJarPath = Premain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File agentJarFile = new File(agentJarPath);

        if (!agentJarFile.exists()) {
            throw new MonitorJarNotFoundException("can not find agent jar in path: " + agentJarPath);
        }

        monitorFolder = agentJarFile.getParentFile();
        if (!monitorFolder.getName().equals(Constants.MONITOR_FOLDER_NAME)) {
            FileHandler logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);
            LOG.severe("init agent error, cause the folder name must be " + Constants.MONITOR_FOLDER_NAME + ", bye bye!");

            throw new MonitorJarNotFoundException("agent jar folder must be: " + Constants.MONITOR_FOLDER_NAME);
        }

        return agentJarPath;
    }

    /**
     * 执行初始化 monitor 采集器
     *
     * @param monitorConfigProperties 监控配置属性
     * @param agentArgs               传入的 agent 参数
     * @param instrumentation         Instrumentation 对象
     * @param agentJarPath            agent jar 文件路径
     * @param logFileHandler          commons logging FileHandler
     * @param monitorCoreJarPath      monitor-core-{version}.jar 文件路径
     * @throws Exception
     */
    private static void doInitMonitor(Properties monitorConfigProperties, String agentArgs, Instrumentation instrumentation, String agentJarPath,
                                      FileHandler logFileHandler, String monitorCoreJarPath) throws Exception {
        Class<?> monitorInitializerClass = Class.forName(Constants.MONITOR_INITIALIZER_CLASS);
        Object monitorInitializer = monitorInitializerClass.newInstance();

        Class<?>[] monitorInitArgs = {Map.class, Properties.class, Instrumentation.class};
        Method monitorInitMethod = monitorInitializer.getClass().getMethod(Constants.MONITOR_INITIALIZER_METHOD, monitorInitArgs);

        Map<String, Object> environment = new HashMap<String, Object>();
        environment.put("monitorFolder", monitorFolder.getPath());
        environment.put("agentArgs", agentArgs);
        environment.put("agentJarPath", agentJarPath);
        environment.put("logFileHandler", logFileHandler);
        environment.put("coreJarPath", monitorCoreJarPath);

        // 反射执行 collector 初始化方法
        monitorInitMethod.invoke(monitorInitializer, environment, monitorConfigProperties, instrumentation);
        LOG.info("monitor initialized successfully!");
    }
}
