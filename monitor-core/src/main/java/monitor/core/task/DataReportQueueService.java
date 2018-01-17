package monitor.core.task;

import monitor.core.MonitorLifecycle;
import monitor.core.log.MonitorLogFactory;
import monitor.core.report.Reporters;
import monitor.core.report.vo.ReportData;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bjlizhitao on 2018/1/17.
 * 监控数据上报队列
 */
public class DataReportQueueService implements MonitorLifecycle {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(DataReportQueueService.class);
    private static final int QUEUE_SIZE = 100;
    private Thread[] threads = new Thread[1];
    /**
     * 上报数据队列
     */
    private ArrayBlockingQueue<ReportData> dataReportQueue = new ArrayBlockingQueue<ReportData>(QUEUE_SIZE);
    private static final DataReportQueueService INSTANCE = new DataReportQueueService();

    private DataReportQueueService() {
    }

    public static DataReportQueueService getInstance() {
        return INSTANCE;
    }

    @Override
    public void start() {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new DataReportThread(i);
            threads[i].start();
        }
    }

    @Override
    public void stop() {
        Thread[] currentThreads = this.threads;

        for (Thread currentThread : currentThreads) {
            try {
                currentThread.interrupt();
                currentThread.join();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "failed to stop DataReportThread", e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    /**
     * 将需要上报的监控数据添加到上报数据队列
     *
     * @param reportData
     * @return
     */
    public boolean addToReportDataQueue(ReportData reportData) {
        return dataReportQueue.offer(reportData);
    }

    /**
     * 数据上报线程
     */
    private class DataReportThread extends Thread {
        public DataReportThread(int i) {
            super("Monitor-DataReportThread-" + i);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    ReportData reportData = DataReportQueueService.this.dataReportQueue.take();

                    if (null != reportData) {
                        Reporters.getReporter().reportData(reportData);
                    }
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "dataReportQueue.take error, cause: ", e);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "failed to send data to dataHub, cause: ", e);
                }
            }
        }
    }
}
