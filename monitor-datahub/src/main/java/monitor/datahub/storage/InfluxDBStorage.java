package monitor.datahub.storage;

/**
 * Created by lizhitao on 2018/1/13.
 * 使用 InfluxDB 存储采集数据
 */
public class InfluxDBStorage implements MonitorStorage {
    @Override
    public boolean storageData(String collectData) {
        return false;
    }
}
