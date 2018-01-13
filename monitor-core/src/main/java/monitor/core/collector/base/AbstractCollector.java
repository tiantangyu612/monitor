package monitor.core.collector.base;

import monitor.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * Collector 基类
 */
public abstract class AbstractCollector implements Collector {
    /**
     * 采集项列表
     */
    private List<CollectorItem> collectorItems = new ArrayList<CollectorItem>();

    /**
     * 采集器是否启用，默认不启用
     */
    private boolean enable = false;

    /**
     * 设置采集器是否启用
     *
     * @param enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 获取采集器是否启用
     *
     * @return
     */
    public boolean isEnable() {
        return this.enable;
    }

    /**
     * 添加采集项
     *
     * @param collectorItem
     */
    protected void addCollectorItem(CollectorItem collectorItem) {
        if (null != collectorItem) {
            this.collectorItems.add(collectorItem);
        }
    }

    /**
     * 获取所有采集项
     *
     * @return
     */
    protected List<CollectorItem> getCollectorItems() {
        return this.collectorItems;
    }

    /**
     * 采集监控数据
     *
     * @return
     */
    public Map<String, List<Map<String, Object>>> collectData() {
        List<CollectorItem> collectorItems = getCollectorItems();

        Map<String, List<Map<String, Object>>> collectDataMap = new HashMap<String, List<Map<String, Object>>>();
        if (CollectionUtils.isNotEmpty(collectorItems)) {
            for (CollectorItem collectorItem : collectorItems) {
                collectDataMap.put(collectorItem.getName(), collectorItem.collectData());
            }
        }

        return collectDataMap;
    }
}
