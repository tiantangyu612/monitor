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
    public final AtomicReference<String> lastError = new AtomicReference();
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

    public void onThrowable(Throwable t, boolean recordStackTrace) {
        this.errorCount.incrementAndGet();
        if (recordStackTrace && this.lastError.get() == null) {
            String s = ExceptionUtil.getThrowableStackTrace(t);
            this.lastError.compareAndSet(null, s);
        }

    }

    public void onThrowable(Throwable t, String stackTrace) {
        this.errorCount.incrementAndGet();
        if (this.lastError.get() == null) {
            this.lastError.compareAndSet(null, stackTrace);
        }

    }

    public void onThrowable(Throwable t) {
        this.errorCount.incrementAndGet();
        if (this.lastError.get() == null) {
            String s = ExceptionUtil.getThrowableStackTrace(t);
            this.lastError.compareAndSet(null, s);
        }

    }

    public void onFinally(long timeUsed) {
        this.invokedCount.incrementAndGet();
        this.invokingCount.decrementAndGet();
        this.totalNanos.addAndGet(timeUsed);
        ConcurrentUtil.setMaxValue(this.maxTime, timeUsed);
        timeUsed /= 1000000L;
        if (timeUsed < 10L) {
            this.ms0_10.incrementAndGet();
        } else if (timeUsed < 100L) {
            this.ms10_100.incrementAndGet();
        } else if (timeUsed < 1000L) {
            this.ms100_1000.incrementAndGet();
        } else if (timeUsed < 10000L) {
            this.s1_10.incrementAndGet();
        } else {
            this.s10_n.incrementAndGet();
        }

    }

    public Map<String, Object> harvest() {
        int ic = this.invokedCount.getAndSet(0);
        if (ic <= 0) {
            return null;
        } else {
            HashMap v = new HashMap();
            v.put("invokedCount", Integer.valueOf(ic));
            v.put("invokingCount", Integer.valueOf(this.invokingCount.get()));
            v.put("totalTime", Integer.valueOf((int) (this.totalNanos.getAndSet(0L) / 1000000L)));
            v.put("errorCount", Integer.valueOf(this.errorCount.getAndSet(0)));
            v.put("lastError", this.lastError.getAndSet(null));
            v.put("maxConcurrency", Integer.valueOf(this.maxConcurrency.getAndSet(0)));
            v.put("maxTime", Integer.valueOf((int) (this.maxTime.getAndSet(0L) / 1000000L)));
            v.put("ms0_10", Integer.valueOf(this.ms0_10.getAndSet(0)));
            v.put("ms10_100", Integer.valueOf(this.ms10_100.getAndSet(0)));
            v.put("ms100_1000", Integer.valueOf(this.ms100_1000.getAndSet(0)));
            v.put("s1_10", Integer.valueOf(this.s1_10.getAndSet(0)));
            v.put("s10_n", Integer.valueOf(this.s10_n.getAndSet(0)));
            return v;
        }
    }
}
