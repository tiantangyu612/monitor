package monitor.core.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * Created by lizhitao on 2018/1/9.
 * 原子 double 类
 */
public class AtomicDouble extends Number implements Serializable {
    private static final long serialVersionUID = 0L;
    private transient volatile long value;
    private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");

    public AtomicDouble(double initialValue) {
        this.value = Double.doubleToRawLongBits(initialValue);
    }

    public AtomicDouble() {
    }

    /**
     * 获取 double value
     *
     * @return
     */
    public final double get() {
        return Double.longBitsToDouble(this.value);
    }

    /**
     * 设置 double value
     *
     * @param newValue
     */
    public final void set(double newValue) {
        long next = Double.doubleToRawLongBits(newValue);
        this.value = next;
    }

    public final void lazySet(int newValue) {
        set(newValue);
    }

    /**
     * 设置并获取新值
     *
     * @param newValue
     * @return
     */
    public final double getAndSet(double newValue) {
        long next = Double.doubleToRawLongBits(newValue);
        return Double.longBitsToDouble(updater.getAndSet(this, next));
    }

    /**
     * CAS 设置新值
     *
     * @param expect
     * @param update
     * @return
     */
    public final boolean compareAndSet(double expect, double update) {
        return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    /**
     * weakCompareAndSet
     *
     * @param expect
     * @param update
     * @return
     */
    public final boolean weakCompareAndSet(double expect, double update) {
        return updater.weakCompareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    /**
     * Atomically adds the given value to the current value.
     *
     * @param delta
     * @return
     */
    public final double getAndAdd(double delta) {
        long current;
        double currentVal;
        long next;
        do {
            current = this.value;
            currentVal = Double.longBitsToDouble(current);
            double nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!updater.compareAndSet(this, current, next));

        return currentVal;
    }

    public final double addAndGet(double delta) {
        long current;
        double nextVal;
        long next;
        do {
            current = this.value;
            double currentVal = Double.longBitsToDouble(current);
            nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!updater.compareAndSet(this, current, next));

        return nextVal;
    }

    public String toString() {
        return Double.toString(this.get());
    }

    public int intValue() {
        return (int) this.get();
    }

    public long longValue() {
        return (long) this.get();
    }

    public float floatValue() {
        return (float) this.get();
    }

    public double doubleValue() {
        return this.get();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeDouble(this.get());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.set(objectInputStream.readDouble());
    }
}
