package monitor.core.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lizhitao on 2018/1/10.
 * 命名线程工厂，通常创建自定义线程时一般都要对线程进行自定义命名，方便使用 jstack 查看线程的运行状态
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_SEQUENCE = new AtomicInteger(1);

    private final AtomicInteger threadNum = new AtomicInteger(1);

    private final String threadNamePrefix;

    private final boolean daemon;

    private final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQUENCE.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        threadNamePrefix = prefix + "-thread-";
        this.daemon = daemon;
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    public Thread newThread(Runnable runnable) {
        String name = threadNamePrefix + threadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup, runnable, name, 0);
        ret.setDaemon(daemon);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}