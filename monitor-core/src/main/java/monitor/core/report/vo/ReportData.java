package monitor.core.report.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/13.
 * 监控采集器上报的数据
 */
public class ReportData {
    /**
     * 应用名
     */
    private String application;
    /**
     * 集群名
     */
    private String cluster;
    /**
     * 实例 ip
     */
    private String instanceIP;
    /**
     * 上报数据的时间
     */
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

    public String getInstanceIP() {
        return instanceIP;
    }

    public void setInstanceIP(String instanceIP) {
        this.instanceIP = instanceIP;
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
                ", instanceIP='" + instanceIP + '\'' +
                ", timestamp=" + timestamp +
                ", reportData=" + reportData +
                '}';
    }
}
