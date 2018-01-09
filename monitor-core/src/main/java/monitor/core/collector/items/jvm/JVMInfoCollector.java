package monitor.core.collector.items.jvm;

import monitor.core.collector.base.AbstractCollector;

/**
 * Created by lizhitao on 2018/1/9.
 * JVM 基础信息收集器
 */
public class JVMInfoCollector extends AbstractCollector {
    private static JVMInfoCollector instance = new JVMInfoCollector();
    private static JVMInfoCollectorItem jvmInfoCollectorItem = new JVMInfoCollectorItem();

    static {
        instance.addCollectorItem(jvmInfoCollectorItem);
    }

    public static JVMInfoCollector getInstance() {
        return JVMInfoCollector.instance;
    }

    @Override
    public String getName() {
        return "JVMInfo";
    }

    @Override
    public boolean isCollectOnStart() {
        return true;
    }
}
