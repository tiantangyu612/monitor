package monitor.core.report;

import monitor.core.config.MonitorConfig;
import monitor.core.log.MonitorLogFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/13.
 * Reporter 管理器
 */
public class Reporters {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(Reporters.class);
    private static final MonitorReporterFactory DEFAULT_REPORTER_FACTORY = new MonitorSocketReporterFactory();

    /**
     * 获取上报数据处理类
     *
     * @return
     */
    public static MonitorReporter getReporter() {
        String factoryClassName = MonitorConfig.getMonitorReporterFactory();

        try {
            Class<?> factoryClass = Class.forName(factoryClassName);
            MonitorReporterFactory monitorReporterFactory = (MonitorReporterFactory) factoryClass.newInstance();
            return monitorReporterFactory.createMonitorReporter();
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "can not find MonitorReporterFactory named: " + factoryClassName, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "can not create MonitorReporterFactory object named: " + factoryClassName, e);
        }
        return DEFAULT_REPORTER_FACTORY.createMonitorReporter();
    }
}
