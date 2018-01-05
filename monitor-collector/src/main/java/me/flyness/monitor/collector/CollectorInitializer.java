package me.flyness.monitor.collector;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lizhitao on 2018/1/5.
 * monitor collector initializer，监控采集器初始化类
 */
public class CollectorInitializer {
    /**
     * 初始化监控系统采集器，该方法由 Agent 反射调用
     *
     * @param environment
     * @param monitorConfigProperties
     * @param instrumentation
     */
    public void initCollector(Map<String, Object> environment, Properties monitorConfigProperties, Instrumentation instrumentation) {
        System.out.println("initCollector....................");
    }
}
