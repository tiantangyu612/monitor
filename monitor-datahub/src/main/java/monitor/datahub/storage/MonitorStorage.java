package monitor.datahub.storage;

/**
 * Created by lizhitao on 2018/1/13.
 * 监控数据存储
 */
public interface MonitorStorage {
    /**
     * 存储监控数据
     *
     * @param collectData
     * @return
     */
    boolean storageData(String collectData);
}
