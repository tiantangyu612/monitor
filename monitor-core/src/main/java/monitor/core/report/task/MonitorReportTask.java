package monitor.core.report.task;

import monitor.core.collector.Collectors;
import monitor.core.collector.base.Collector;
import monitor.core.report.MonitorReporterFactory;
import monitor.core.util.concurrent.NamedThreadFactory;

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
                Map<String, List<Map<String, Object>>> collectDatas = collector.collectData();
                MonitorReporterFactory.createMonitorRepoter().reportData(collectDatas);
            }
        }

    }

    /**
     * 启动数据上报
     */
    public void start() {
        scheduledFuture = monitorReportSchedule.scheduleWithFixedDelay(MonitorReportTask.instance, 20, 20, TimeUnit.SECONDS);
    }

    /**
     * 停止数据上报
     */
    public void stop() {
        scheduledFuture.cancel(true);
        monitorReportSchedule.shutdown();
    }
}
