package monitor.core.collector.items.jvm;

import monitor.core.collector.base.AbstractCollectorItem;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * jvm 内存信息采集
 */
public class JVMMemoryCollectorItem extends AbstractCollectorItem {
    private MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Override
    public String getName() {
        return "memory";
    }

    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectData = new ArrayList<Map<String, Object>>(1);

        Map<String, Object> jvmMemoryInfo = new HashMap<String, Object>(3);
        jvmMemoryInfo.put("heapMemoryUsage", this.memoryMXBean.getHeapMemoryUsage().getUsed());
        jvmMemoryInfo.put("nonHeapMemoryUsage", this.memoryMXBean.getNonHeapMemoryUsage().getUsed());
        jvmMemoryInfo.put("objectPendingFinalizationCount", this.memoryMXBean.getObjectPendingFinalizationCount());
        collectData.add(jvmMemoryInfo);

        return collectData;
    }
}
