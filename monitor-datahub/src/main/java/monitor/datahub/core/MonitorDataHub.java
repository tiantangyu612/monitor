package monitor.datahub.core;

/**
 * Created by lizhitao on 2018/1/11.
 * MonitorDataHub
 */
public interface MonitorDataHub {
    /**
     * 启动数据收集
     */
    void start();

    /**
     * 停止数据收集
     */
    void stop();
}
