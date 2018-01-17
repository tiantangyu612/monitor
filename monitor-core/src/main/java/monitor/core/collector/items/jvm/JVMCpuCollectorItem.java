package monitor.core.collector.items.jvm;

import monitor.core.collector.base.OneRowCollectorItem;
import monitor.core.log.MonitorLogFactory;
import monitor.core.util.CollectionUtils;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/8.
 * JVM cpu 信息采集
 */
public class JVMCpuCollectorItem extends OneRowCollectorItem {
    private static Logger LOGGER = MonitorLogFactory.getLogger(JVMCpuCollectorItem.class);
    private OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    private MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    private ObjectName operatingSystemBean = null;
    private int processorCount = 1;
    private Long lastProcessCpuTime = null;
    private Long lastSystemTime = null;

    public JVMCpuCollectorItem() {
        init();
    }

    private void init() {
        try {
            this.processorCount = this.operatingSystemMXBean.getAvailableProcessors();
            ObjectName operatingSystem = new ObjectName("java.lang:type=OperatingSystem");
            Set<ObjectName> objectNames = this.mBeanServer.queryNames(operatingSystem, null);
            ObjectName objectName;
            if (CollectionUtils.isNotEmpty(objectNames)) {
                for (Iterator iterator = objectNames.iterator(); iterator.hasNext(); this.operatingSystemBean = objectName) {
                    objectName = (ObjectName) iterator.next();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "failed to get operatingSystem mbean", e);
        }

        if (this.operatingSystemBean == null) {
            LOGGER.log(Level.SEVERE, "operatingSystem mbean is null");
        }

        this.lastProcessCpuTime = this.getProcessCpuTime();
        this.lastSystemTime = System.nanoTime();
    }

    /**
     * 获取处理器 cpu time
     *
     * @return
     */
    private Long getProcessCpuTime() {
        if (this.operatingSystemBean == null) {
            return null;
        } else {
            try {
                Object processCpuTimeObject = this.mBeanServer.getAttribute(this.operatingSystemBean, "ProcessCpuTime");
                if (processCpuTimeObject != null) {
                    try {
                        return Long.parseLong(processCpuTimeObject.toString());
                    } catch (Exception e) {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    public String getName() {
        return "cpu";
    }

    @Override
    protected Map<String, Object> collectItemData() {

        Long currentProcessTime = this.getProcessCpuTime();
        if (currentProcessTime != null && this.lastProcessCpuTime != null) {
            long currentSystemTime = System.nanoTime();
            long cpuTimeInterval = currentProcessTime - this.lastProcessCpuTime;
            long sysTimeInterval = currentSystemTime - this.lastSystemTime;
            double ratio = (double) cpuTimeInterval / (double) sysTimeInterval * 100.0D / (double) this.processorCount;

            Map<String, Object> cpuInfo = new HashMap<String, Object>(5);
            cpuInfo.put("cpuTimeInterval", cpuTimeInterval);
            cpuInfo.put("totalCpuTime", currentProcessTime);
            cpuInfo.put("processorCount", this.processorCount);
            cpuInfo.put("systemTimeInterval", sysTimeInterval);
            cpuInfo.put("cpuRatio", ratio);
            this.lastProcessCpuTime = currentProcessTime;
            this.lastSystemTime = currentSystemTime;

            return cpuInfo;
        }

        return null;
    }
}
