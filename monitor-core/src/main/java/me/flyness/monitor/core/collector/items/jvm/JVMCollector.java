package me.flyness.monitor.core.collector.items.jvm;

import me.flyness.monitor.core.collector.base.AbstractCollector;

/**
 * Created by lizhitao on 2018/1/8.
 * JVM 采集器
 */
public class JVMCollector extends AbstractCollector {
    private static JVMCollector instance = new JVMCollector();
    /**
     * JVM 垃圾回收信息采集项
     */
    private static JVMGCCollectorItem jvmGCCollectorItem = new JVMGCCollectorItem();
    /**
     * JVM 内存信息采集项
     */
    private static JVMMemoryCollectorItem jvmMemoryCollectorItem = new JVMMemoryCollectorItem();
    /**
     * JVM 线程信息采集项
     */
    private static JVMThreadCollectorItem jvmThreadCollectorItem = new JVMThreadCollectorItem();


    static {
        instance.addCollectorItem(jvmGCCollectorItem);
        instance.addCollectorItem(jvmMemoryCollectorItem);
        instance.addCollectorItem(jvmThreadCollectorItem);
    }

    private JVMCollector() {
    }

    public static JVMCollector getInstance() {
        return JVMCollector.instance;
    }

    @Override
    public String getName() {
        return "JVM";
    }
}
