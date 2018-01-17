package monitor.core.collector.items.jvm;

import monitor.core.collector.base.OneRowCollectorItem;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * JVM 类加载信息收集
 */
public class JVMClassLoadingCollectorItem extends OneRowCollectorItem {
    private ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

    @Override
    public String getName() {
        return "classLoading";
    }

    @Override
    protected Map<String, Object> collectItemData() {
        Map<String, Object> classLoadingInfo = new HashMap<String, Object>(3);
        classLoadingInfo.put("loadedClassCount", this.classLoadingMXBean.getLoadedClassCount());
        classLoadingInfo.put("totalLoadedClassCount", this.classLoadingMXBean.getTotalLoadedClassCount());
        classLoadingInfo.put("unloadedClassCount", this.classLoadingMXBean.getUnloadedClassCount());
        return classLoadingInfo;
    }
}
