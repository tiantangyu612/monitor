package monitor.core.report;

import monitor.core.config.MonitorConfig;
import monitor.core.log.MonitorLogFactory;
import monitor.core.report.reporter.SocketReporterFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/13.
 * Reporter 管理器
 */
public class Reporters {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(Reporters.class);
    private static final ReporterFactory DEFAULT_REPORTER_FACTORY = new SocketReporterFactory();

    /**
     * 获取上报数据处理类
     *
     * @return
     */
    public static Reporter getReporter() {
        String factoryClassName = MonitorConfig.getMonitorReporterFactory();

        try {
            Class<?> factoryClass = Class.forName(factoryClassName);
            ReporterFactory reporterFactory = (ReporterFactory) factoryClass.newInstance();
            return reporterFactory.createReporter();
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "can not find ReporterFactory named: " + factoryClassName, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "can not create ReporterFactory object named: " + factoryClassName, e);
        }
        return DEFAULT_REPORTER_FACTORY.createReporter();
    }
}
