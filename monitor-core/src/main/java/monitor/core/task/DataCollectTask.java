package monitor.core.task;

import monitor.core.MonitorLifecycle;
import monitor.core.collector.Collectors;
import monitor.core.collector.base.Collector;
import monitor.core.config.MonitorConfig;
import monitor.core.log.MonitorLogFactory;
import monitor.core.report.vo.ReportData;
import monitor.core.util.NetUtil;
import monitor.core.util.concurrent.NamedThreadFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/10.
 * 监控数据采集定时上报任务
 */
public class DataCollectTask implements Runnable, MonitorLifecycle {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(DataCollectTask.class);
    private static DataCollectTask instance = new DataCollectTask();

    private DataCollectTask() {
    }

    public static DataCollectTask getInstance() {
        return DataCollectTask.instance;
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
                reportData.setInstanceIP(NetUtil.getIp());
                reportData.setTimestamp(System.currentTimeMillis());

                Map<String, Map<String, List<Map<String, Object>>>> reportDataMap = new HashMap<String, Map<String, List<Map<String, Object>>>>();
                Map<String, List<Map<String, Object>>> collectData = collector.collectData();
                reportDataMap.put(collector.getName(), collectData);

                reportData.setReportData(reportDataMap);

                DataReportQueueService.getInstance().addToReportDataQueue(reportData);
            }
        }

    }

    /**
     * 启动数据上报
     */
    @Override
    public void start() {
        scheduledFuture = monitorReportSchedule.scheduleWithFixedDelay(DataCollectTask.instance, 30, 30, TimeUnit.SECONDS);
    }

    /**
     * 停止数据上报
     */
    @Override
    public void stop() {
        try {
            scheduledFuture.cancel(false);
            monitorReportSchedule.shutdown();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "failed to stop DataCollectTask", e);
        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
