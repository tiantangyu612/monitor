package monitor.core.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lizhitao on 2018/1/9.
 * ConcurrentUtil，并发工具类
 */
public class ConcurrentUtil {
    private ConcurrentUtil() {
    }

    /**
     * 设置 AtomicInteger 最小值
     *
     * @param current
     * @param minValue
     */
    public static void setMinValue(AtomicInteger current, int minValue) {
        int currentValue;
        do {
            currentValue = current.get();
        } while (minValue < currentValue && !current.compareAndSet(currentValue, minValue));
    }

    /**
     * 设置 AtomicLong 最小值
     *
     * @param current
     * @param minValue
     */
    public static void setMinValue(AtomicLong current, long minValue) {
        long currentValue;
        do {
            currentValue = current.get();
        } while (minValue < currentValue && !current.compareAndSet(currentValue, minValue));

    }

    /**
     * 设置 AtomicDouble 最小值
     *
     * @param current
     * @param minValue
     */
    public static void setMinValue(AtomicDouble current, double minValue) {
        double currentValue;
        do {
            currentValue = current.get();
        } while (minValue < currentValue && !current.compareAndSet(currentValue, minValue));

    }

    /**
     * 设置 AtomicInteger 最大值
     *
     * @param current
     * @param maxValue
     */
    public static boolean setMaxValue(AtomicInteger current, int maxValue) {
        while (true) {
            int currentValue = current.get();
            if (maxValue > currentValue) {
                if (!current.compareAndSet(currentValue, maxValue)) {
                    continue;
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 设置 AtomicLong 最大值
     *
     * @param current
     * @param maxValue
     */
    public static boolean setMaxValue(AtomicLong current, long maxValue) {
        while (true) {
            long currentValue = current.get();
            if (maxValue > currentValue) {
                if (!current.compareAndSet(currentValue, maxValue)) {
                    continue;
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 设置 AtomicDouble 最大值
     *
     * @param current
     * @param maxValue
     */
    public static void setMaxValue(AtomicDouble current, double maxValue) {
        double currentValue;
        do {
            currentValue = current.get();
        } while (maxValue > currentValue && !current.compareAndSet(currentValue, maxValue));
    }
}