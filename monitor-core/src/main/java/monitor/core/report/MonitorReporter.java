package monitor.core.report;

import monitor.core.report.vo.ReportData;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/10.
 * 监控数据上报
 */
public interface MonitorReporter {
    /**
     * 上报监控数据
     *
     * @param reportData
     * @return
     */
    boolean reportData(ReportData reportData);
}
