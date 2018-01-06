package me.flyness.monitor;

import me.flyness.monitor.config.CollectorConfig;
import me.flyness.monitor.config.MonitorConfig;
import me.flyness.monitor.env.MonitorEnv;
import me.flyness.monitor.log.CollectorLogFactory;
import me.flyness.monitor.transformer.Transformers;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * 监控初始化类，初始化监控环境、监控配置、Transformer、采集器配置并启动监控采集器
 */
public class MonitorInitializer {
    private static Logger LOG = CollectorLogFactory.getLogger(MonitorInitializer.class);

    /**
     * 初始化监控系统，该方法由 Agent premain 反射调用
     *
     * @param environment
     * @param monitorConfigProperties
     * @param instrumentation
     */
    public void initMonitor(Map<String, Object> environment, Properties monitorConfigProperties, Instrumentation instrumentation) {
        MonitorEnv monitorEnv = new MonitorEnv(environment);

        // 初始化日志配置
        initLog(monitorEnv);
        LOG.info("int collector log success!");

        // 初始化监控配置
        MonitorConfig.initConfig(monitorEnv, monitorConfigProperties);
        LOG.info("init monitor config success!");

        // 初始化 Transformers
        Transformers.initTransformers(instrumentation);
        LOG.info("init transformers success!");

        // 初始化监控采集器配置信息
        CollectorConfig.initConfig();
        LOG.info("init collector config success!");
    }

    /**
     * 初始化日志配置
     *
     * @param monitorEnv
     */
    private void initLog(MonitorEnv monitorEnv) {
        FileHandler logFileHandler = monitorEnv.getLogFileHandler();
        CollectorLogFactory.initLog(logFileHandler);
    }
}
