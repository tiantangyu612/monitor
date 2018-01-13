package monitor.datahub.storage.influxdb;

import monitor.datahub.storage.InfluxDBStorage;
import monitor.datahub.storage.MonitorStorage;
import monitor.datahub.storage.MonitorStorageFactory;

/**
 * Created by lizhitao on 2018/1/13.
 * InfluxDBStorageFactory
 */
public class InfluxDBStorageFactory implements MonitorStorageFactory {
    @Override
    public MonitorStorage crateMonitorStorage() {
        return new InfluxDBStorage();
    }
}
