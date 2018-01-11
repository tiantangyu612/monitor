package monitor.core;

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
    /**
     * monitor core jar 路径
     */
    private String monitorCoreJarPath;
    /**
     * monitor core jar 版本号
     */
    private String monitorCoreJarVersion;

    public MonitorEnv(Map<String, Object> environment) {
        this.monitorFolder = (String) environment.get("monitorFolder");
        this.agentArgs = (String) environment.get("agentArgs");
        this.agentJarPath = (String) environment.get("agentJarPath");
        this.logFileHandler = (FileHandler) environment.get("logFileHandler");
        this.monitorCoreJarPath = (String) environment.get("monitorCoreJarPath");
        this.monitorCoreJarVersion = (String) environment.get("monitorCoreJarVersion");
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

    public String getMonitorCoreJarPath() {
        return monitorCoreJarPath;
    }

    public void setMonitorCoreJarPath(String monitorCoreJarPath) {
        this.monitorCoreJarPath = monitorCoreJarPath;
    }

    public String getMonitorCoreJarVersion() {
        return monitorCoreJarVersion;
    }

    public void setMonitorCoreJarVersion(String monitorCoreJarVersion) {
        this.monitorCoreJarVersion = monitorCoreJarVersion;
    }

    @Override
    public String toString() {
        return "MonitorEnv{" +
                "monitorFolder='" + monitorFolder + '\'' +
                ", agentArgs='" + agentArgs + '\'' +
                ", agentJarPath='" + agentJarPath + '\'' +
                ", logFileHandler=" + logFileHandler +
                ", monitorCoreJarPath='" + monitorCoreJarPath + '\'' +
                ", monitorCoreJarVersion='" + monitorCoreJarVersion + '\'' +
                '}';
    }
}
