package monitor.core.collector.base;

/**
 * Created by lizhitao on 2018/1/8.
 * 采集项基类
 */
public abstract class AbstractCollectorItem implements CollectorItem {
    /**
     * 一个采集周期内的最大采集数据条数
     */
    private int maxCollectCount = 500;

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
}
