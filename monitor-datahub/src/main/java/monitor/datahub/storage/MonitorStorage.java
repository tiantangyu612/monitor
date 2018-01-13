package monitor.datahub.storage;

import java.io.Closeable;

/**
 * Created by lizhitao on 2018/1/13.
 * 监控数据存储
 */
public interface MonitorStorage extends Closeable {
    /**
     * 存储监控数据
     *
     * @param reportDataJson
     * @return
     */
    boolean storageData(String reportDataJson);
}
