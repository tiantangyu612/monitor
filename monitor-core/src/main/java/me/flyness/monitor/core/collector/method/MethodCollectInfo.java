package me.flyness.monitor.core.collector.method;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lizhitao on 2018/1/6.
 * java 方法监控收集信息
 */
public class MethodCollectInfo {
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
}
