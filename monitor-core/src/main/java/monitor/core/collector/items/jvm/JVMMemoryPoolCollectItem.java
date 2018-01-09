package monitor.core.collector.items.jvm;

import monitor.core.log.MonitorLogFactory;
import monitor.core.collector.base.AbstractCollectorItem;
import monitor.core.util.CollectionUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/8.
 * JVM 内存池信息采集
 */
public class JVMMemoryPoolCollectItem extends AbstractCollectorItem {
    private static Logger LOGGER = MonitorLogFactory.getLogger(JVMMemoryPoolCollectItem.class);

    @Override
    public String getName() {
        return "memoryPool";
    }

    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectDatas = new ArrayList<Map<String, Object>>();

        try {
            List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
            if (CollectionUtils.isNotEmpty(memoryPoolMXBeans)) {
                for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
                    MemoryUsage memoryUsage = memoryPoolMXBean.getUsage();
                    Map<String, Object> memoryInfo = new HashMap<String, Object>();
                    memoryInfo.put("name", memoryPoolMXBean.getName());
                    memoryInfo.put("committed", memoryUsage.getCommitted());
                    memoryInfo.put("init", memoryUsage.getInit());
                    memoryInfo.put("max", memoryUsage.getMax());
                    memoryInfo.put("used", memoryUsage.getUsed());

                    collectDatas.add(memoryInfo);
                }

            }
        } catch (Throwable throwable) {
            LOGGER.log(Level.SEVERE, "failed to get operatingSystem mbean", throwable);
        }

        return collectDatas;
    }
}
