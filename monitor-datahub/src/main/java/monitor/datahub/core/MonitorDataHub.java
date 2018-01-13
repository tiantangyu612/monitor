package monitor.datahub.core;

import monitor.datahub.storage.MonitorStorageFactory;

/**
 * Created by lizhitao on 2018/1/11.
 * MonitorDataHub
 */
public interface MonitorDataHub {
    /**
     * 设置存储
     *
     * @param monitorStorageFactory
     */
    void setMonitorStorageFactory(MonitorStorageFactory monitorStorageFactory);

    /**
     * 启动数据收集
     */
    void start();

    /**
     * 停止数据收集
     */
    void stop();
}
