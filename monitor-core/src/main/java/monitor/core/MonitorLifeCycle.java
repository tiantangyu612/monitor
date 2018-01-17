package monitor.core;

/**
 * Created by bjlizhitao on 2018/1/17.
 * 监控生命周期
 */
public interface MonitorLifecycle {
    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();
}
