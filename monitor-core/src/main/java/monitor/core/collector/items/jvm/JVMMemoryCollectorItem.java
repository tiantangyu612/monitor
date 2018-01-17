package monitor.core.collector.items.jvm;

import monitor.core.collector.base.OneRowCollectorItem;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * jvm 内存信息采集
 */
public class JVMMemoryCollectorItem extends OneRowCollectorItem {
    private MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Override
    public String getName() {
        return "memory";
    }

    @Override
    protected Map<String, Object> collectItemData() {
        Map<String, Object> jvmMemoryInfo = new HashMap<String, Object>(3);
        jvmMemoryInfo.put("heapMemoryUsage", this.memoryMXBean.getHeapMemoryUsage().getUsed());
        jvmMemoryInfo.put("nonHeapMemoryUsage", this.memoryMXBean.getNonHeapMemoryUsage().getUsed());
        jvmMemoryInfo.put("objectPendingFinalizationCount", this.memoryMXBean.getObjectPendingFinalizationCount());

        return jvmMemoryInfo;
    }
}
