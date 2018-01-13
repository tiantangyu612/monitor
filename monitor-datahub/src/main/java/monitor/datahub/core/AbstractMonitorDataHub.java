package monitor.datahub.core;

import monitor.datahub.storage.MonitorStorageFactory;

/**
 * Created by lizhitao on 2018/1/13.
 * 监控数据中心基类
 */
public abstract class AbstractMonitorDataHub implements MonitorDataHub {
    private MonitorStorageFactory monitorStorageFactory;

    public MonitorStorageFactory getMonitorStorageFactory() {
        return monitorStorageFactory;
    }

    @Override
    public void setMonitorStorageFactory(MonitorStorageFactory monitorStorageFactory) {
        this.monitorStorageFactory = monitorStorageFactory;
    }


}
