package monitor.core.report;

import monitor.core.config.MonitorConfig;
import monitor.core.log.MonitorLogFactory;
import monitor.core.report.vo.DataHubUrl;
import monitor.core.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bjlizhitao on 2018/1/17.
 * 基础监控数据上报器
 */
public abstract class AbstractMonitorReporter implements MonitorReporter {
    private final Logger logger = MonitorLogFactory.getLogger(getClass());
    /**
     * dataHub url 分隔符
     */
    private static final String URL_SPLIT = ",";
    /**
     * dataHub url 端口号分隔符
     */
    private static final String PORT_SPLIT = ":";
    /**
     * 默认 dataHub 开启端口号
     */
    private static final int DEFAULT_PORT = 16666;

    /**
     * 解析并获取 dataHub url 列表
     *
     * @return
     */
    protected List<DataHubUrl> getDataHubUrls() {
        List<DataHubUrl> dataHubUrls = new ArrayList<DataHubUrl>();

        String dataHubUrl = MonitorConfig.getDataHubUrl();
        String[] urls = dataHubUrl.split(URL_SPLIT);
        for (String url : urls) {
            if (StringUtils.isNotBlank(url)) {
                try {
                    if (url.contains(PORT_SPLIT)) {
                        String[] hostAndPort = url.split(PORT_SPLIT);
                        String host = hostAndPort[0].trim();
                        int port = Integer.valueOf(hostAndPort[1].trim());
                        dataHubUrls.add(new DataHubUrl(host, port));
                    } else {
                        dataHubUrls.add(new DataHubUrl(url, DEFAULT_PORT));
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "error parse dataHubUrl: " + url, e);
                }
            }
        }

        return dataHubUrls;
    }
}
