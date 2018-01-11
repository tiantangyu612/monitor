package monitor.agent.config;

import monitor.agent.exception.PropertyNotFoundException;
import monitor.agent.log.AgentLoggerFactory;
import monitor.agent.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/11.
 * MonitorConfigProperties
 */
public class MonitorConfigProperties {
    private static final Logger LOG = AgentLoggerFactory.getLogger(MonitorConfigProperties.class);

    /**
     * 加载监控配置
     *
     * @param agentArgs
     * @param monitorFolder
     * @return
     * @throws Exception
     */
    public static Properties loadProperties(String agentArgs, File monitorFolder) throws Exception {
        File configPropertiesFile = new File(monitorFolder.getPath() + File.separator + Constants.CONFIG_FILE_NAME);

        Properties monitorConfigProperties = new Properties();
        FileHandler logFileHandler;

        try {
            monitorConfigProperties.load(new FileInputStream(configPropertiesFile));

            Object applicationProperty = monitorConfigProperties.get("application");
            if (applicationProperty == null) {
                LOG.severe("application property can not be null!");
                throw new PropertyNotFoundException("application property is null");
            }
            String application = applicationProperty.toString();

            Object clusterProperty = monitorConfigProperties.getProperty("cluster");
            String cluster;
            if (clusterProperty == null) {
                cluster = Constants.DEFAULT_CLUSTER_NAME;
            } else {
                cluster = clusterProperty.toString();
            }

            logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, application, cluster);
            LOG.addHandler(logFileHandler);

            monitorConfigProperties.setProperty("cluster", cluster);
            return monitorConfigProperties;
        } catch (FileNotFoundException e) {
            logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);

            String errorMsg = "can not find monitor.properties in: " + monitorFolder.getPath();
            LOG.log(Level.SEVERE, errorMsg, e);
            throw e;
        } catch (IOException e) {
            logFileHandler = AgentLoggerFactory.buildLogFileHandler(agentArgs, null, null);
            LOG.addHandler(logFileHandler);

            String errorMsg = "IOException while reading monitor.properties!";
            LOG.log(Level.SEVERE, errorMsg, e);
            throw e;
        }
    }
}
