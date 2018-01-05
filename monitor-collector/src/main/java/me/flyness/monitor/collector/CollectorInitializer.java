package me.flyness.monitor.collector;

import me.flyness.monitor.collector.environment.MonitorEnvironment;
import me.flyness.monitor.collector.log.CollectorLogFactory;
import me.flyness.monitor.collector.transformer.JavaMethodTransformer;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * monitor collector initializer，监控采集器初始化类
 */
public class CollectorInitializer {
    private static Logger LOG = CollectorLogFactory.getLogger(CollectorInitializer.class);
    /**
     * 监控配置属性
     */
    private static Properties monitorConfigProperties;

    /**
     * 初始化监控系统采集器，该方法由 Agent 反射调用
     *
     * @param environment
     * @param monitorConfigProperties
     * @param instrumentation
     */
    public void initCollector(Map<String, Object> environment, Properties monitorConfigProperties, Instrumentation instrumentation) {
        CollectorInitializer.monitorConfigProperties = monitorConfigProperties;

        MonitorEnvironment monitorEnvironment = new MonitorEnvironment(environment);

        FileHandler logFileHandler = monitorEnvironment.getLogFileHandler();
        CollectorLogFactory.initLog(logFileHandler);

        String application = monitorConfigProperties.getProperty("application");
        String instance = monitorConfigProperties.getProperty("instance");

        instrumentation.addTransformer(new JavaMethodTransformer());
    }

    /**
     * 获取监控配置属性
     *
     * @return
     */
    public static Properties getMonitorConfigProperties() {
        return CollectorInitializer.monitorConfigProperties;
    }
}
