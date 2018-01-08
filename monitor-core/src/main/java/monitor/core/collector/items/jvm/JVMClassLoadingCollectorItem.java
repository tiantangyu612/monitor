package monitor.core.collector.items.jvm;

import monitor.core.collector.base.AbstractCollectorItem;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * JVM 类加载信息收集
 */
public class JVMClassLoadingCollectorItem extends AbstractCollectorItem {
    private ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

    @Override
    public String getName() {
        return "classLoading";
    }

    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectData = new ArrayList<Map<String, Object>>(1);

        Map<String, Object> classLoadingInfo = new HashMap<String, Object>(3);
        classLoadingInfo.put("loadedClassCount", this.classLoadingMXBean.getLoadedClassCount());
        classLoadingInfo.put("totalLoadedClassCount", this.classLoadingMXBean.getTotalLoadedClassCount());
        classLoadingInfo.put("unloadedClassCount", this.classLoadingMXBean.getUnloadedClassCount());
        collectData.add(classLoadingInfo);

        return collectData;
    }
}
