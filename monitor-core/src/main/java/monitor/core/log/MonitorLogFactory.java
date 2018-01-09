package monitor.core.log;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * 监控 logFactory，使用 java logging
 */
public class MonitorLogFactory {
    private static FileHandler logFileHandler;

    /**
     * 初始化 Logger
     *
     * @param logFileHandler
     */
    public static void initLog(FileHandler logFileHandler) {
        MonitorLogFactory.logFileHandler = logFileHandler;
    }

    /**
     * 获取 Logger
     *
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        if (logFileHandler != null) {
            logger.addHandler(logFileHandler);
        }

        logger.setUseParentHandlers(false);
        return logger;
    }
}
