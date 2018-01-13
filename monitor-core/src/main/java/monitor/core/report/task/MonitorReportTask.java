package monitor.core.report.task;

import monitor.core.collector.Collectors;
import monitor.core.collector.base.Collector;
import monitor.core.config.MonitorConfig;
import monitor.core.report.Reporters;
import monitor.core.report.vo.ReportData;
import monitor.core.util.concurrent.NamedThreadFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhitao on 2018/1/10.
 * 监控数据上报任务
 */
public class MonitorReportTask implements Runnable {
    private static MonitorReportTask instance = new MonitorReportTask();

    private MonitorReportTask() {
    }

    public static MonitorReportTask getInstance() {
        return MonitorReportTask.instance;
    }

    private ScheduledExecutorService monitorReportSchedule = Executors.newScheduledThreadPool(1, new NamedThreadFactory("monitor-collector", true));
    private ScheduledFuture scheduledFuture;

    @Override
    public void run() {
        Map<String, Collector> collectorMap = Collectors.getAllCollectors();
        for (Map.Entry<String, Collector> collectorEntry : collectorMap.entrySet()) {
            Collector collector = collectorEntry.getValue();
            if (collector.isEnable()) {
                ReportData reportData = new ReportData();
                reportData.setApplication(MonitorConfig.getApplication());
                reportData.setCluster(MonitorConfig.getCluster());
                reportData.setTimestamp(System.currentTimeMillis());

                Map<String, Map<String, List<Map<String, Object>>>> reportDataMap = new HashMap<String, Map<String, List<Map<String, Object>>>>();
                Map<String, List<Map<String, Object>>> collectData = collector.collectData();
                reportDataMap.put(collector.getName(), collectData);

                reportData.setReportData(reportDataMap);

                Reporters.getReporter().reportData(reportData);
            }
        }

    }

    /**
     * 启动数据上报
     */
    public void start() {
        scheduledFuture = monitorReportSchedule.scheduleWithFixedDelay(MonitorReportTask.instance, 30, 30, TimeUnit.SECONDS);
    }

    /**
     * 停止数据上报
     */
    public void stop() {
        scheduledFuture.cancel(true);
        monitorReportSchedule.shutdown();
    }
}
