package monitor.core.report;

/**
 * Created by lizhitao on 2018/1/13.
 * socket 上报采集数据处理类工厂
 */
public class MonitorSocketReporterFactory implements MonitorReporterFactory {
    @Override
    public MonitorReporter createMonitorReporter() {
        return new MonitorSocketReporter();
    }
}
