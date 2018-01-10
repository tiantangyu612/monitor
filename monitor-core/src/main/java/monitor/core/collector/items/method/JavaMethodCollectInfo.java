package monitor.core.collector.items.method;

import monitor.core.util.ConcurrentUtil;
import monitor.core.util.ExceptionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lizhitao on 2018/1/6.
 * java 方法监控收集信息
 */
public class JavaMethodCollectInfo {
    /**
     * 方法已经执行的次数
     */
    public final AtomicInteger invokedCount = new AtomicInteger(0);
    /**
     * 正在执行该方法的线程数
     */
    public final AtomicInteger invokingCount = new AtomicInteger(0);
    /**
     * 执行总时间，单位为纳秒
     */
    public final AtomicLong totalNanos = new AtomicLong(0L);
    /**
     * 执行方法出现错误的次数
     */
    public final AtomicInteger errorCount = new AtomicInteger(0);
    /**
     * 最长执行时间
     */
    public final AtomicLong maxTime = new AtomicLong(0L);
    /**
     * 执行方法的错误信息
     */
    public final AtomicReference<String> lastError = new AtomicReference<String>();
    /**
     * 执行该方法的最大并发数
     */
    public final AtomicInteger maxConcurrency = new AtomicInteger(0);
    /**
     * 执行方法时间在 0~10 ms 之间的个数
     */
    public final AtomicInteger ms0_10 = new AtomicInteger(0);
    /**
     * 执行方法时间在 10~100 ms 之间的个数
     */
    public final AtomicInteger ms10_100 = new AtomicInteger(0);
    /**
     * 执行方法时间在 100~1000 ms 之间的个数
     */
    public final AtomicInteger ms100_1000 = new AtomicInteger(0);
    /**
     * 执行方法时间在 1~10 s 之间的个数
     */
    public final AtomicInteger s1_10 = new AtomicInteger(0);
    /**
     * 执行方法时间在 10 s 以上的个数
     */
    public final AtomicInteger s10_n = new AtomicInteger(0);

    /**
     * 进入方法时调用
     */
    public void onStart() {
        int invokingCount = this.invokingCount.incrementAndGet();
        ConcurrentUtil.setMaxValue(this.maxConcurrency, invokingCount);
    }

    /**
     * 方法抛异常时调用，并判断是否需要记录异常堆栈信息
     *
     * @param t
     * @param recordStackTrace
     */
    public void onThrowable(Throwable t, boolean recordStackTrace) {
        this.errorCount.incrementAndGet();
        if (recordStackTrace && this.lastError.get() == null) {
            String stackTrace = ExceptionUtil.getStackTrace(t);
            this.lastError.compareAndSet(null, stackTrace);
        }

    }

    /**
     * 方法抛异常时调用，并记录异常堆栈信息
     *
     * @param t
     * @param stackTrace
     */
    public void onThrowable(Throwable t, String stackTrace) {
        this.errorCount.incrementAndGet();
        if (this.lastError.get() == null) {
            this.lastError.compareAndSet(null, stackTrace);
        }

    }

    /**
     * 方法抛异常时调用，并记录异常堆栈信息
     *
     * @param t
     */
    public void onThrowable(Throwable t) {
        this.errorCount.incrementAndGet();
        if (this.lastError.get() == null) {
            String stackTrace = ExceptionUtil.getStackTrace(t);
            this.lastError.compareAndSet(null, stackTrace);
        }

    }

    /**
     * 方法结束时调用
     *
     * @param methodInvokeTime
     */
    public void onFinally(long methodInvokeTime) {
        this.invokedCount.incrementAndGet();
        this.invokingCount.decrementAndGet();
        this.totalNanos.addAndGet(methodInvokeTime);
        ConcurrentUtil.setMaxValue(this.maxTime, methodInvokeTime);

        // 转换为 ms
        methodInvokeTime /= 1000000L;
        if (methodInvokeTime < 10L) {
            this.ms0_10.incrementAndGet();
        } else if (methodInvokeTime < 100L) {
            this.ms10_100.incrementAndGet();
        } else if (methodInvokeTime < 1000L) {
            this.ms100_1000.incrementAndGet();
        } else if (methodInvokeTime < 10000L) {
            this.s1_10.incrementAndGet();
        } else {
            this.s10_n.incrementAndGet();
        }
    }

    /**
     * 采集数据
     *
     * @return
     */
    public Map<String, Object> collectData() {
        int invokedCount = this.invokedCount.getAndSet(0);
        if (invokedCount <= 0) {
            return null;
        } else {
            Map<String, Object> methodCollectData = new HashMap<String, Object>();
            methodCollectData.put("invokedCount", invokedCount);
            methodCollectData.put("invokingCount", this.invokingCount.get());
            methodCollectData.put("totalTime", (int) (this.totalNanos.getAndSet(0L) / 1000000L));
            methodCollectData.put("errorCount", this.errorCount.getAndSet(0));
            methodCollectData.put("lastError", this.lastError.getAndSet(null));
            methodCollectData.put("maxConcurrency", this.maxConcurrency.getAndSet(0));
            methodCollectData.put("maxTime", (int) (this.maxTime.getAndSet(0L) / 1000000L));
            methodCollectData.put("ms0_10", this.ms0_10.getAndSet(0));
            methodCollectData.put("ms10_100", this.ms10_100.getAndSet(0));
            methodCollectData.put("ms100_1000", this.ms100_1000.getAndSet(0));
            methodCollectData.put("s1_10", this.s1_10.getAndSet(0));
            methodCollectData.put("s10_n", this.s10_n.getAndSet(0));
            return methodCollectData;
        }
    }

    @Override
    public String toString() {
        return "JavaMethodCollectInfo{" +
                "invokedCount=" + invokedCount +
                ", invokingCount=" + invokingCount +
                ", totalNanos=" + totalNanos +
                ", errorCount=" + errorCount +
                ", maxTime=" + maxTime +
                ", lastError=" + lastError +
                ", maxConcurrency=" + maxConcurrency +
                ", ms0_10=" + ms0_10 +
                ", ms10_100=" + ms10_100 +
                ", ms100_1000=" + ms100_1000 +
                ", s1_10=" + s1_10 +
                ", s10_n=" + s10_n +
                '}';
    }
}
