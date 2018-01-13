package monitor.core.report.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/13.
 * 上报的数据
 */
public class ReportData {
    private String application;
    private String cluster;
    private long timestamp;
    private Map<String, Map<String, List<Map<String, Object>>>> reportData;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Map<String, List<Map<String, Object>>>> getReportData() {
        return reportData;
    }

    public void setReportData(Map<String, Map<String, List<Map<String, Object>>>> reportData) {
        this.reportData = reportData;
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "application='" + application + '\'' +
                ", cluster='" + cluster + '\'' +
                ", timestamp=" + timestamp +
                ", reportData=" + reportData +
                '}';
    }
}
