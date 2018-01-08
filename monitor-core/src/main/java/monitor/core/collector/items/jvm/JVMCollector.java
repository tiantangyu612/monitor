package monitor.core.collector.items.jvm;

import monitor.core.collector.base.AbstractCollector;

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
    /**
     * JVM 类加载信息采集项
     */
    private static JVMClassLoadingCollectorItem jvmClassLoadingCollectorItem = new JVMClassLoadingCollectorItem();
    /**
     * JVM 编译信息采集项
     */
    private static JVMCompileCollectorItem jvmCompileCollectorItem = new JVMCompileCollectorItem();
    /**
     * JVM cpu 信息采集
     */
    private static JVMCpuCollectorItem jvmCpuCollectorItem = new JVMCpuCollectorItem();
    /**
     * JVM memory pool 信息采集
     */
    private static JVMMemoryPoolCollectItem jvmMemoryPoolCollectItem = new JVMMemoryPoolCollectItem();


    static {
        instance.addCollectorItem(jvmGCCollectorItem);
        instance.addCollectorItem(jvmMemoryCollectorItem);
        instance.addCollectorItem(jvmThreadCollectorItem);
        instance.addCollectorItem(jvmClassLoadingCollectorItem);
        instance.addCollectorItem(jvmCompileCollectorItem);
        instance.addCollectorItem(jvmCpuCollectorItem);
        instance.addCollectorItem(jvmMemoryPoolCollectItem);
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
