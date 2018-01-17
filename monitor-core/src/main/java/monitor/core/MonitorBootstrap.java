package monitor.core;

import javassist.ClassPool;
import javassist.NotFoundException;
import monitor.core.collector.Collectors;
import monitor.core.config.MonitorConfig;
import monitor.core.log.MonitorLogFactory;
import monitor.core.task.DataCollectTask;
import monitor.core.task.DataReportQueueService;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * 监控初始化类，初始化监控环境、监控配置、Transformer、采集器配置并启动监控采集器
 */
public class MonitorBootstrap implements MonitorLifecycle {
    private static Logger LOG = MonitorLogFactory.getLogger(MonitorBootstrap.class);
    private List<MonitorLifecycle> monitorComponents = new ArrayList<MonitorLifecycle>();

    /**
     * 初始化监控系统，该方法由 Agent premain 反射调用
     *
     * @param environment
     * @param monitorConfigProperties
     * @param instrumentation
     * @throws Exception
     */
    public void init(Map<String, Object> environment, Properties monitorConfigProperties, Instrumentation instrumentation) throws Exception {
        MonitorEnv monitorEnv = new MonitorEnv(environment);

        // 初始化日志配置
        initLog(monitorEnv);
        LOG.info("int collector log success!");

        // 将 monitor-core jar 添加到 classpath，防止 javassist 字节码增强时出现 ClassNotFoundException
        addMonitorCoreJarToClasspath(monitorEnv);

        // 初始化监控配置
        MonitorConfig.initConfig(monitorEnv, monitorConfigProperties);
        LOG.info("init monitor config success!");

        // 初始化监控采集器配置信息
        Collectors.initCollectors(instrumentation);
        LOG.info("init collectors success!");

        // 初始化监控组件
        initMonitorComponent();

        // 启动采集器
        start();
    }

    /**
     * 初始化日志配置
     *
     * @param monitorEnv
     */
    private void initLog(MonitorEnv monitorEnv) {
        FileHandler logFileHandler = monitorEnv.getLogFileHandler();
        MonitorLogFactory.initLog(logFileHandler);
    }

    /**
     * 初始化监控组件
     */
    private void initMonitorComponent() {
        monitorComponents.add(DataCollectTask.getInstance());
        monitorComponents.add(DataReportQueueService.getInstance());
    }

    /**
     * 将 monitor-core jar 添加到 classpath
     *
     * @param monitorEnv
     * @return
     */
    private void addMonitorCoreJarToClasspath(MonitorEnv monitorEnv) throws NotFoundException {
        String monitorCoreJarPath = monitorEnv.getCoreJarPath();
        ClassPool classPool = ClassPool.getDefault();

        try {
            classPool.get(this.getClass().getName());
        } catch (NotFoundException e) {
            try {
                classPool.insertClassPath(monitorCoreJarPath);
            } catch (NotFoundException e1) {
                LOG.severe("failed to insert javassist class path:" + monitorCoreJarPath);
                throw e;
            }
        }
    }

    /**
     * 启动采集器
     */
    @Override
    public void start() {
        for (MonitorLifecycle monitorComponent : monitorComponents) {
            try {
                monitorComponent.start();
            } catch (Exception e) {
                // NOP
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                MonitorBootstrap.this.stop();
            }
        });
    }

    /**
     * 停止采集器
     */
    @Override
    public void stop() {
        for (MonitorLifecycle monitorComponent : monitorComponents) {
            try {
                monitorComponent.stop();
            } catch (Exception e) {
                // NOP
            }
        }
    }
}
