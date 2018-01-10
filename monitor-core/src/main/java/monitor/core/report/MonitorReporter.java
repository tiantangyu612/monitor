package monitor.core.report;

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
     * @param monitorData
     * @return
     */
    boolean reportData(Map<String, List<Map<String, Object>>> monitorData);
}
