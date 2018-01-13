package monitor.datahub.core;

/**
 * Created by lizhitao on 2018/1/13.
 * Socket dataHub Factory
 */
public class MonitorSocketDataHubFactory implements MonitorDataHubFactory {
    @Override
    public MonitorDataHub createMonitorDataHub() {
        return new SocketDataHub();
    }
}
