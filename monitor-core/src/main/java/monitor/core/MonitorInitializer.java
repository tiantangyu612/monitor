package monitor.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import monitor.core.collector.Collectors;
import monitor.core.collector.base.Collector;
import monitor.core.config.MonitorConfig;
import monitor.core.log.MonitorLogFactory;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * 监控初始化类，初始化监控环境、监控配置、Transformer、采集器配置并启动监控采集器
 */
public class MonitorInitializer {
    private static Logger LOG = MonitorLogFactory.getLogger(MonitorInitializer.class);

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

        // 初始化监控采集器配置信息
        Collectors.initCollectors(instrumentation);
        LOG.info("init collectors success!");

        // 启动采集器
        startCollector();
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
     * 启动采集器
     */
    private static void startCollector() {
        Map<String, Collector> collectorMap = Collectors.getAllCollectors();
        for (Map.Entry<String, Collector> collectorEntry : collectorMap.entrySet()) {
            Map<String, List<Map<String, Object>>> collectDatas = collectorEntry.getValue().collectData();

            for (Map.Entry<String, List<Map<String, Object>>> collectDatasEntry : collectDatas.entrySet()) {
                System.out.println(JSON.toJSONString(collectDatasEntry, SerializerFeature.PrettyFormat));
            }
        }
    }
}
