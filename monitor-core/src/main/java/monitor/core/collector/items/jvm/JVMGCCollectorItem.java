package monitor.core.collector.items.jvm;

import monitor.core.log.MonitorLogFactory;
import monitor.core.collector.base.AbstractCollectorItem;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/8.
 * jvm GC 信息收集
 */
public class JVMGCCollectorItem extends AbstractCollectorItem {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(JVMGCCollectorItem.class);
    private GarbageCollectorMXBean youngGC;
    private GarbageCollectorMXBean fullGC;
    private long lastYoungGCCollectionCount = -1L;
    private long lastYoungGCCollectionTime = -1L;
    private long lastFullGCCollectionCount = -1L;
    private long lastFullGCCollectionTime = -1L;

    private static Set<String> youngGCNames = new HashSet<String>();
    private static Set<String> fullGCNames = new HashSet<String>();

    static {
        youngGCNames.add("ParNew");
        youngGCNames.add("Copy");
        youngGCNames.add("PS Scavenge");
        youngGCNames.add("G1 Young Generation");
        youngGCNames.add("Garbage collection optimized for short pausetimes Young Collector");
        youngGCNames.add("Garbage collection optimized for throughput Young Collector");
        youngGCNames.add("Garbage collection optimized for deterministic pausetimes Young Collector");

        fullGCNames.add("ConcurrentMarkSweep");
        fullGCNames.add("MarkSweepCompact");
        fullGCNames.add("PS MarkSweep");
        fullGCNames.add("G1 Old Generation");
        fullGCNames.add("Garbage collection optimized for short pausetimes Old Collector");
        fullGCNames.add("Garbage collection optimized for throughput Old Collector");
        fullGCNames.add("Garbage collection optimized for deterministic pausetimes Old Collector");
    }


    public JVMGCCollectorItem() {
        init();
    }

    private void init() {
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            if (null != fullGC && fullGCNames.contains(garbageCollectorMXBean.getName())) {
                this.fullGC = garbageCollectorMXBean;
            }

            if (null != this.youngGC && youngGCNames.contains(garbageCollectorMXBean.getName())) {
                this.youngGC = garbageCollectorMXBean;
            }

            if (null != youngGC && null != fullGC) {
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "GC";
    }

    /**
     * 收集 GC 数据
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectData = new ArrayList<Map<String, Object>>(1);

        Map<String, Object> jvmGCInfo = new HashMap<String, Object>(10);
        jvmGCInfo.put("youngGCCollectionCount", this.getYoungGCCollectionCount());
        jvmGCInfo.put("youngGCCollectionTime", this.getYoungGCCollectionTime());
        jvmGCInfo.put("fullGCCollectionCount", this.getFullGCCollectionCount());
        jvmGCInfo.put("fullGCCollectionTime", this.getFullGCCollectionTime());
//        jvmGCInfo.put("youngGCCollectionTotalCount", this.getYoungGCCollectionTotalCount());
//        jvmGCInfo.put("youngGCCollectionTotalTime", this.getYoungGCCollectionTotalTime());
//        jvmGCInfo.put("fullGCCollectionTotalCount", this.getFullGCCollectionTotalCount());
//        jvmGCInfo.put("fullGCCollectionTotalTime", this.getFullGCCollectionTotalTime());

        if (this.fullGC != null) {
            jvmGCInfo.put("fullGCMBeanName", this.fullGC.getName());
        }

        if (this.youngGC != null) {
            jvmGCInfo.put("youngGCMBeanName", this.youngGC.getName());
        }

        collectData.add(jvmGCInfo);
        return collectData;
    }

    /**
     * 获取 young gc 总次数
     *
     * @return
     */
    private long getYoungGCCollectionTotalCount() {
        return this.youngGC == null ? 0L : this.youngGC.getCollectionCount();
    }

    /**
     * 获取 young gc 总时间
     *
     * @return
     */
    private long getYoungGCCollectionTotalTime() {
        return this.youngGC == null ? 0L : this.youngGC.getCollectionTime();
    }

    /**
     * 获取 full gc 总次数
     *
     * @return
     */
    private long getFullGCCollectionTotalCount() {
        return this.fullGC == null ? 0L : this.fullGC.getCollectionCount();
    }

    /**
     * 获取 full gc 总时间
     *
     * @return
     */
    private long getFullGCCollectionTotalTime() {
        return this.fullGC == null ? 0L : this.fullGC.getCollectionTime();
    }

    /**
     * 获取采集周期内的 young gc 次数
     *
     * @return
     */
    private long getYoungGCCollectionCount() {
        long current = this.getYoungGCCollectionTotalCount();
        if (this.lastYoungGCCollectionCount == -1L) {
            this.lastYoungGCCollectionCount = current;
            return current;
        } else {
            long reslut = current - this.lastYoungGCCollectionCount;
            this.lastYoungGCCollectionCount = current;
            return reslut;
        }
    }

    /**
     * 获取采集周期内的 young gc 时间
     *
     * @return
     */
    private long getYoungGCCollectionTime() {
        long current = this.getYoungGCCollectionTotalTime();
        if (this.lastYoungGCCollectionTime == -1L) {
            this.lastYoungGCCollectionTime = current;
            return current;
        } else {
            long reslut = current - this.lastYoungGCCollectionTime;
            this.lastYoungGCCollectionTime = current;
            return reslut;
        }
    }

    /**
     * 获取采集周期内的 full gc 次数
     *
     * @return
     */
    private long getFullGCCollectionCount() {
        long current = this.getFullGCCollectionTotalCount();
        if (this.lastFullGCCollectionCount == -1L) {
            this.lastFullGCCollectionCount = current;
            return current;
        } else {
            long reslut = current - this.lastFullGCCollectionCount;
            this.lastFullGCCollectionCount = current;
            return reslut;
        }
    }

    /**
     * 获取采集周期内的 full gc 时间
     *
     * @return
     */
    private long getFullGCCollectionTime() {
        long current = this.getFullGCCollectionTotalTime();
        if (this.lastFullGCCollectionTime == -1L) {
            this.lastFullGCCollectionTime = current;
            return current;
        } else {
            long reslut = current - this.lastFullGCCollectionTime;
            this.lastFullGCCollectionTime = current;
            return reslut;
        }
    }
}
