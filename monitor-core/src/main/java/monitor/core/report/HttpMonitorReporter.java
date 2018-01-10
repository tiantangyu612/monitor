package monitor.core.report;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import monitor.core.log.MonitorLogFactory;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/10.
 * 使用 Http 接口上报数据
 */
public class HttpMonitorReporter implements MonitorReporter {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(HttpMonitorReporter.class);

    /**
     * 上报数据
     *
     * @param monitorData
     * @return
     */
    @Override
    public boolean reportData(Map<String, List<Map<String, Object>>> monitorData) {
        try {
            String monitorJsonData = JSON.toJSONString(monitorData, SerializerFeature.PrettyFormat);

            System.out.println(monitorJsonData);
            LOGGER.info(monitorJsonData);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
