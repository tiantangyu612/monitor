package me.flyness.monitor.core.collector.base;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * 采集项
 */
public interface CollectorItem {

    /**
     * 获取采集项的名称
     *
     * @return
     */
    String getName();

    /**
     * 是否启用该采集项
     *
     * @param enable
     */
    void setEnable(boolean enable);

    /**
     * 获取采集项是否启用
     *
     * @return
     */
    boolean isEnable();

    /**
     * 设置一个采集周期内的最大采集数据条数
     *
     * @param maxCollectCount
     */
    void setMaxCollectCount(int maxCollectCount);

    /**
     * 获取一个采集周期内的最大采集数据条数
     *
     * @param maxCollectCount
     * @return
     */
    int getMaxCollectCount(int maxCollectCount);

    /**
     * 采集监控数据
     *
     * @return
     */
    List<Map<String, Object>> collectData();

    /**
     * 设置是否需要上报监控数据到监控数据中心
     *
     * @param needReport
     */
    void setNeedReport(boolean needReport);

    /**
     * 获取是否需要上报监控数据到监控数据中心状态
     *
     * @return
     */
    boolean isNeedReport();
}