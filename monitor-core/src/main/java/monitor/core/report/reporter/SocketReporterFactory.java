package monitor.core.report.reporter;

import monitor.core.report.Reporter;
import monitor.core.report.ReporterFactory;

/**
 * Created by lizhitao on 2018/1/13.
 * socket 上报采集数据处理类工厂
 */
public class SocketReporterFactory implements ReporterFactory {
    @Override
    public Reporter createReporter() {
        return new SocketReporter();
    }
}
