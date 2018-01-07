package me.flyness.monitor.agent.log;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by bjlizhitao on 2018/1/4.
 * agent logFactory，使用 java logging
 */
public class AgentLoggerFactory {
    /**
     * 1 MB 字节数
     */
    private static final int ONE_MB_BYTES = 1048576;
    /**
     * 日志文件个数
     */
    public static int LOG_FILE_COUNT = 3;
    /**
     * 日志文件大小，100 MB
     */
    public static int LOG_FILE_SIZE = 100 * ONE_MB_BYTES;

    /**
     * 获取 Logger
     *
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);

        return logger;
    }

    /**
     * 构建 LogFileHandler
     *
     * @param logRoot     日志根路径
     * @param application 应用名称
     * @param cluster     集群名称
     * @return
     */
    public static FileHandler buildLogFileHandler(String logRoot, String application, String cluster) {
        // 获取日志根文件夹路径
        String logRootFolderPath = getLogRootFolderPath(logRoot);
        // 获取监控日志文件夹路径
        String logFileFolderPath = logRootFolderPath + File.separator + "monitor-logs";
        // 创建监控日志文件夹
        createLogFolder(logFileFolderPath);
        // 获取监控日志文件路径
        String logFilepath = getLogFilePath(logFileFolderPath, application, cluster);

        try {
            FileHandler logFileHandler = new FileHandler(logFilepath, LOG_FILE_SIZE, LOG_FILE_COUNT);
            logFileHandler.setFormatter(new SimpleFormatter());
            return logFileHandler;
        } catch (Exception e) {
            throw new RuntimeException("buildLogFileHandler error, filePath is:" + logFilepath);
        }
    }

    /**
     * 获取 logRootFolderPath，若 logRoot 为 null 则获取 user.home 路径，若仍为空则为 /tmp
     *
     * @param logRoot
     * @return
     */
    private static String getLogRootFolderPath(String logRoot) {
        String logFilePath = null;
        if (logRoot != null) {
            File file = new File(logRoot.trim());
            if ((file.isDirectory()) && (file.canWrite())) {
                logFilePath = logRoot;
            }
        }

        if (logFilePath == null) {
            logFilePath = (String) System.getProperties().get("user.home");
            if (logFilePath == null) {
                logFilePath = "/tmp";
            }
        }

        return logFilePath;
    }

    /**
     * 创建文件夹
     *
     * @param folderPath
     */
    private static void createLogFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    /**
     * 获取监控日志文件路径
     *
     * @param logFileFolderPath 日志文件夹路径
     * @param application       应用名
     * @param cluster           集群名
     * @return
     */
    private static String getLogFilePath(String logFileFolderPath, String application, String cluster) {
        String logFilePath;
        if (application == null) {
            logFilePath = logFileFolderPath + File.separator + "error.log";
        } else {
            if ((cluster == null) || (cluster.equals("default"))) {
                logFilePath = logFileFolderPath + File.separator + "monitor_" + application + ".log";
            } else {
                logFilePath = logFileFolderPath + File.separator + "monitor_" + application + "-" + cluster + ".log";
            }
        }

        return logFilePath;
    }
}