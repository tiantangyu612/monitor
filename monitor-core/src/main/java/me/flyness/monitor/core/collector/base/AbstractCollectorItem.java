package me.flyness.monitor.core.collector.base;

/**
 * Created by lizhitao on 2018/1/8.
 * 采集项基类
 */
public abstract class AbstractCollectorItem implements CollectorItem {
    /**
     * 是否启用该采集项
     */
    private boolean enable = true;
    /**
     * 一个采集周期内的最大采集数据条数
     */
    private int maxCollectCount = 500;
    /**
     * 是否需要上报监控数据到监控数据中心
     */
    private boolean needReport = true;

    /**
     * 是否启用该采集项
     *
     * @param enable
     */
    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 获取采集项是否启用
     *
     * @return
     */
    @Override
    public boolean isEnable() {
        return this.enable;
    }

    /**
     * 设置一个采集周期内的最大采集数据条数
     *
     * @param maxCollectCount
     */
    @Override
    public void setMaxCollectCount(int maxCollectCount) {
        this.maxCollectCount = maxCollectCount;
    }

    /**
     * 获取一个采集周期内的最大采集数据条数
     *
     * @param maxCollectCount
     * @return
     */
    public int getMaxCollectCount(int maxCollectCount) {
        return this.maxCollectCount;
    }

    /**
     * 设置是否需要上报监控数据到监控数据中心
     *
     * @param needReport
     */
    @Override
    public void setNeedReport(boolean needReport) {
        this.needReport = needReport;
    }

    /**
     * 获取是否需要上报监控数据到监控数据中心状态
     *
     * @return
     */
    @Override
    public boolean isNeedReport() {
        return this.needReport;
    }
}
