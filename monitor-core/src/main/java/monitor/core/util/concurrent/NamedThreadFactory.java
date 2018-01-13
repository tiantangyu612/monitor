package monitor.core.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lizhitao on 2018/1/10.
 * 命名线程工厂，通常创建自定义线程时一般都要对线程进行自定义命名，方便使用 jstack 查看线程的运行状态
 */
public class NamedThreadFactory implements ThreadFactory {
    /**
     * 线程池编号
     */
    private static final AtomicInteger POOL_SEQUENCE = new AtomicInteger(1);
    /**
     * 线程数
     */
    private final AtomicInteger threadNum = new AtomicInteger(1);
    /**
     * 线程名称前缀
     */
    private final String threadNamePrefix;
    /**
     * 是否为后台线程
     */
    private final boolean daemon;
    /**
     * 线程租
     */
    private final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQUENCE.getAndIncrement(), false);
    }

    /**
     * @param prefix 线程名称前缀
     */
    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    /**
     * @param prefix 线程名称前缀
     * @param daemon 是否为后台线程
     */
    public NamedThreadFactory(String prefix, boolean daemon) {
        threadNamePrefix = prefix + "-thread-";
        this.daemon = daemon;
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    /**
     * 创建一个新线程
     *
     * @param runnable
     * @return
     */
    public Thread newThread(Runnable runnable) {
        String name = threadNamePrefix + threadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup, runnable, name, 0);
        ret.setDaemon(daemon);
        return ret;
    }

    /**
     * 获取线程组
     *
     * @return
     */
    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}