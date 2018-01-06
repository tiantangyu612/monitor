package me.flyness.monitor.env;

import java.util.Map;
import java.util.logging.FileHandler;

/**
 * Created by lizhitao on 2018/1/5.
 * monitor 环境配置
 */
public class MonitorEnv {
    /**
     * monitor 文件夹路径
     */
    private String monitorFolder;
    /**
     * java agent args
     */
    private String agentArgs;
    /**
     * monitor agent jar 路径
     */
    private String agentJarPath;
    /**
     * 日志 logFileHandler
     */
    private FileHandler logFileHandler;

    public MonitorEnv(Map<String, Object> environment) {
        this.monitorFolder = (String) environment.get("monitorFolder");
        this.agentArgs = (String) environment.get("agentArgs");
        this.agentJarPath = (String) environment.get("agentJarPath");
        this.logFileHandler = (FileHandler) environment.get("logFileHandler");
    }

    public String getMonitorFolder() {
        return monitorFolder;
    }

    public void setMonitorFolder(String monitorFolder) {
        this.monitorFolder = monitorFolder;
    }

    public String getAgentArgs() {
        return agentArgs;
    }

    public void setAgentArgs(String agentArgs) {
        this.agentArgs = agentArgs;
    }

    public String getAgentJarPath() {
        return agentJarPath;
    }

    public void setAgentJarPath(String agentJarPath) {
        this.agentJarPath = agentJarPath;
    }

    public FileHandler getLogFileHandler() {
        return logFileHandler;
    }

    public void setLogFileHandler(FileHandler logFileHandler) {
        this.logFileHandler = logFileHandler;
    }

    @Override
    public String toString() {
        return "MonitorEnv{" +
                "monitorFolder='" + monitorFolder + '\'' +
                ", agentArgs='" + agentArgs + '\'' +
                ", agentJarPath='" + agentJarPath + '\'' +
                ", logFileHandler=" + logFileHandler +
                '}';
    }
}
