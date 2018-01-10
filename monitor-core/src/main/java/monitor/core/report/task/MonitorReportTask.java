package monitor.core.report.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import monitor.core.collector.Collectors;
import monitor.core.collector.base.Collector;
import monitor.core.report.MonitorReporterFactory;
import monitor.core.util.concurrent.NamedThreadFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhitao on 2018/1/10.
 * 监控数据上报任务
 */
public class MonitorReportTask implements Runnable {
    private static MonitorReportTask instance = new MonitorReportTask();

    private MonitorReportTask() {
    }

    public static final ScheduledExecutorService monitorReportSchedule = Executors.newScheduledThreadPool(1, new NamedThreadFactory("monitor-collector", true));

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
    public static void start() {
        MonitorReportTask.monitorReportSchedule.scheduleWithFixedDelay(MonitorReportTask.instance, 20, 10, TimeUnit.SECONDS);
    }

    /**
     * 停止数据上报
     */
    public static void stop() {
        MonitorReportTask.monitorReportSchedule.shutdown();
    }
}
