package monitor.core.collector.items.jvm;

import monitor.core.collector.base.OneRowCollectorItem;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * jvm 线程信息采集
 */
public class JVMThreadCollectorItem extends OneRowCollectorItem {
    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    @Override
    public String getName() {
        return "thread";
    }

    @Override
    protected Map<String, Object> collectItemData() {
        Map<String, Object> jvmThreadInfo = new HashMap<String, Object>(8);
        jvmThreadInfo.put("threadCount", Integer.valueOf(this.threadMXBean.getThreadCount()));
        jvmThreadInfo.put("peakThreadCount", Integer.valueOf(this.threadMXBean.getPeakThreadCount()));
        this.threadMXBean.resetPeakThreadCount();
        jvmThreadInfo.put("daemonThreadCount", Integer.valueOf(this.threadMXBean.getDaemonThreadCount()));
        jvmThreadInfo.put("deadlockedThreadsCount", Integer.valueOf(this.threadMXBean.findDeadlockedThreads() == null ? 0 : this.threadMXBean.findDeadlockedThreads().length));
        jvmThreadInfo.put("totalStartedThreadCount", Long.valueOf(this.threadMXBean.getTotalStartedThreadCount()));
        jvmThreadInfo.put("currentThreadCpuTime", Long.valueOf(this.threadMXBean.getCurrentThreadCpuTime()));
        jvmThreadInfo.put("currentThreadUserTime", Long.valueOf(this.threadMXBean.getCurrentThreadUserTime()));
        jvmThreadInfo.put("monitorDeadlockedThreads", Integer.valueOf(this.threadMXBean.findMonitorDeadlockedThreads() == null ? 0 : this.threadMXBean.findMonitorDeadlockedThreads().length));

        return jvmThreadInfo;
    }
}
