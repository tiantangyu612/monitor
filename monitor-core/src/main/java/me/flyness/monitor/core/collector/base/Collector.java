package me.flyness.monitor.core.collector.base;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * Collector 接口
 */
public interface Collector {
    /**
     * 获取采集器的名称
     *
     * @return
     */
    String getName();

    /**
     * 是否启用该采集器
     *
     * @param enable
     */
    void setEnable(boolean enable);

    /**
     * 获取采集器是否启用
     *
     * @return
     */
    boolean isEnable();

    /**
     * 采集监控数据
     *
     * @return
     */
    Map<String, List<Map<String, Object>>> collectData();

    /**
     * 是否项目启动时收集
     *
     * @return
     */
    boolean isCollectOnStart();
}
