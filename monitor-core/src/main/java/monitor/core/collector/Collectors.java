package monitor.core.collector;

import monitor.core.collector.base.Collector;
import monitor.core.collector.base.transformer.MonitorTransformer;
import monitor.core.collector.items.jvm.JVMCollector;
import monitor.core.collector.items.jvm.JVMInfoCollector;
import monitor.core.collector.items.method.JavaMethodCollector;
import monitor.core.collector.items.method.JavaMethodTransformer;
import monitor.core.collector.items.tomcat.TomcatCollector;
import monitor.core.config.MonitorConfig;
import monitor.core.util.StringUtils;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/6.
 * 采集器管理类
 */
public class Collectors {
    private static Map<String, Collector> collectorMap = new HashMap<String, Collector>();

    /**
     * 初始化采集器
     *
     * @param instrumentation
     */
    public static void initCollectors(Instrumentation instrumentation) {
        MonitorTransformer monitorTransformer = new MonitorTransformer();

        if (MonitorConfig.isEnableJVMInfoCollect()) {
            // 添加 JVM 采集器
            addCollector(JVMCollector.getInstance());
            addCollector(JVMInfoCollector.getInstance());
        }

        if (MonitorConfig.isEnableJavaMethodCollect()) {
            // 字节码增强需要监控的 java method
            monitorTransformer.addTransformer(new JavaMethodTransformer());
            // 添加 java method 采集器
            addCollector(JavaMethodCollector.getInstance());
        }

        if (MonitorConfig.isEnableTomcatCollect()) {
            // 添加 tomcat 采集器
            addCollector(TomcatCollector.getInstance());
        }

        instrumentation.addTransformer(monitorTransformer);
    }

    /**
     * 添加采集器
     *
     * @param collector
     */
    public static void addCollector(Collector collector) {
        if (null != collector && StringUtils.isNotBlank(collector.getName())) {
            // TODO 此处后续增加服务器端配置，可以动态开启或关闭采集器
            collector.setEnable(true);

            Collectors.collectorMap.put(collector.getName().trim(), collector);
        }
    }

    /**
     * 获取所有的采集器
     *
     * @return
     */
    public static Map<String, Collector> getAllCollectors() {
        return Collectors.collectorMap;
    }

    /**
     * 根据采集器名称获取采集器
     *
     * @param collectorName
     * @return
     */
    public static Collector getCollector(String collectorName) {
        return Collectors.collectorMap.get(collectorName);
    }
}
