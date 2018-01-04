package me.flyness.monitor.agent;

import me.flyness.monitor.agent.log.MonitorLoggerFactory;
import me.flyness.monitor.agent.util.MonitorJarVersionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
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

    private static File MONITOR_AGNET_FILE = null;
    public static String MONITOR_AGNET_NAME = "monitor-agent";

    public static String AGENT_ARGS = "agent_args";
    public static String PREMAIN_JAR_PATH = "premain_jar_path";
    public static String COLLECTOR_JAR_PATH = "collector_jar_path";
    public static String COLLECTOR_PREFIX = "sentry-javaagent-collector-";
    public static String COLLECTOR_SUFFIX = ".jar";
    public static String CONFIG_FILE_NAME = "sentry.properties";

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
        String agentJarPath = Agent.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File jarFile = new File(agentJarPath);
        MONITOR_AGNET_FILE = jarFile.getParentFile();

        if (!MONITOR_AGNET_FILE.getName().equals(MONITOR_AGNET_NAME)) {
            FileHandler fhandler = MonitorLoggerFactory.buildFileHandler(agentArgs, null, null);
            LOG.addHandler(fhandler);
            LOG.log(Level.SEVERE, " the folder name must be sentry-javaagent-home agent quit!");
            return;
        }
        File propf = new File(MONITOR_AGNET_FILE.getPath() + File.separator + CONFIG_FILE_NAME);

        Properties sentryProperties = new Properties();
        String appName;
        String instance;
        FileHandler fhandler;

        try {
            sentryProperties.load(new FileInputStream(propf));
            appName = (String) sentryProperties.get("appName");

            instance = (String) sentryProperties.get("instance");
            if (instance == null) {
                instance = System.getProperty("sentry_app_instance_name");
            }
            if (instance == null) {
                instance = "default";
            }
            fhandler = MonitorLoggerFactory.buildFileHandler(agentArgs, appName, instance);
            LOG.addHandler(fhandler);
            if (appName == null) {
                LOG.log(Level.SEVERE, "appName property not exists!");
                return;
            }
        } catch (FileNotFoundException e) {
            fhandler = MonitorLoggerFactory.buildFileHandler(agentArgs, null, null);
            LOG.addHandler(fhandler);

            String s = "sentry.properties file not exist in :" + MONITOR_AGNET_FILE.getPath();

            LOG.log(Level.SEVERE, s, e);
            return;
        } catch (IOException e) {
            fhandler = MonitorLoggerFactory.buildFileHandler(agentArgs, null, null);
            LOG.addHandler(fhandler);
            String s = "IOException reading in reading sentry.properties!";
            LOG.log(Level.SEVERE, s, e);
            return;
        }
        Object collectorLibPath = System.getProperties().get("sentry_collector_libpath");
        if (collectorLibPath == null) {
            LOG.log(Level.SEVERE, "collectorLibPath property not found in system property ");
            return;
        }
        List<String> collectorList = searchAgentCollector(new File(collectorLibPath.toString()));
        if (collectorList.isEmpty()) {
            LOG.log(Level.SEVERE, "not found collector file named with " + COLLECTOR_PREFIX + ".{version}.jar:" + collectorLibPath.toString());
            return;
        }
        LOG.info("found collectors:" + collectorList.toString());

        String collectorjar = MonitorJarVersionUtil.getHighestMonitorCollectorJar(collectorList);
        LOG.info("use higest version:" + collectorjar);
        File f = new File(collectorjar);
        if (!f.exists()) {
            LOG.severe("collectorjar file not exist:" + collectorjar);
            return;
        }
        if (!f.isFile()) {
            LOG.severe("collectorjar file not a file:" + collectorjar);
            return;
        }
        JarFile jf = new JarFile(collectorjar);
        instrumentation.appendToBootstrapClassLoaderSearch(jf);

        LOG.info(" added " + collectorjar + " to  BootstrapClassLoaderSearch path");

        initCollector(sentryProperties, agentArgs, instrumentation, agentJarPath, collectorjar, fhandler, appName, instance);

        LOG.info("collector initialized successfully!");
    }


    private static void initCollector(Properties sentryProperties, String agentArgs, Instrumentation inst, String agentJarPath, String collectorPath, FileHandler fhandler, String appName, String instance)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, SecurityException, NoSuchMethodException, ClassNotFoundException {
        String collectorClassName = "com.netease.sentry.javaagent.collector.CollectorInitializer";
        Class<?> clazz = Class.forName(collectorClassName);

        Class<?>[] agentInitArgs = {Map.class, Properties.class, Instrumentation.class};
        Object main = clazz.newInstance();
        Method initMethod = main.getClass().getMethod("initAgent", agentInitArgs);
        Map<String, Object> parameters = new HashMap();
        parameters.put("sentry_javaagent_home", MONITOR_AGNET_FILE.getPath());
        parameters.put("agent_args", agentArgs);
        parameters.put("agent_jar_file", agentJarPath);
        parameters.put("collector_jar_path", collectorPath);
        parameters.put("log_file_handler", fhandler);
        parameters.put("appName", appName);
        parameters.put("instance", instance);
        initMethod.invoke(main, new Object[]{parameters, sentryProperties, inst});
    }

    private static List<String> searchAgentCollector(File libPath) {
        if (!libPath.exists()) {
            LOG.severe("not exist:" + libPath);
            return Collections.emptyList();
        }
        if (!libPath.isDirectory()) {
            LOG.severe("not a directory:" + libPath);
            return Collections.emptyList();
        }
        File[] children = libPath.listFiles();
        if ((children == null) || (children.length == 0)) {
            LOG.severe("no children:" + libPath);
            return Collections.emptyList();
        }
        List<String> result = new ArrayList(3);
        for (File ff : children) {
            String fname = ff.getName();
            if ((ff.isFile()) && (fname.startsWith(COLLECTOR_PREFIX)) && (fname.endsWith(COLLECTOR_SUFFIX))) {
                result.add(ff.getPath());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String premainJarPath = Agent.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File jarFile = new File(premainJarPath);
        System.out.println(jarFile.getParentFile());
    }
}
