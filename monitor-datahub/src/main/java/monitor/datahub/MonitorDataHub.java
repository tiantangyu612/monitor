package monitor.datahub;

/**
 * Created by lizhitao on 2018/1/11.
 * MonitorDataHub
 */
public interface MonitorDataHub {
    /**
     * 收集采集器采集的数据
     *
     * @param collectData
     */
    void collectData(String collectData);
}
