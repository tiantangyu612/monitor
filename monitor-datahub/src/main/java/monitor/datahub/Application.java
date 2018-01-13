package monitor.datahub;

import monitor.datahub.core.MonitorDataHub;
import monitor.datahub.core.MonitorDataHubFactory;
import monitor.datahub.core.MonitorSocketDataHubFactory;
import monitor.datahub.storage.influxdb.InfluxDBStorageFactory;

/**
 * Created by lizhitao on 2018/1/13.
 * 应用启动器
 */
public class Application {
    public static void main(String[] args) {
        MonitorDataHubFactory monitorDataHubFactory = new MonitorSocketDataHubFactory();
        final MonitorDataHub monitorDataHub = monitorDataHubFactory.createMonitorDataHub();
        monitorDataHub.setMonitorStorageFactory(new InfluxDBStorageFactory());
        monitorDataHub.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                monitorDataHub.stop();
            }
        });
    }
}
