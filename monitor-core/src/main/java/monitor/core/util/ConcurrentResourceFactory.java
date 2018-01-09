package monitor.core.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentResourceFactory<T, R> {
    private R[] resources;
    private AtomicReference<T>[] values;
    private LinkedBlockingQueue<Integer> linkedBlockingQueue;

    public ConcurrentResourceFactory(int size) {
        if (size > 5012) {
            throw new RuntimeException("more than 5012:" + size);
        } else if (size <= 0) {
            throw new RuntimeException("less than zero:" + size);
        } else {
            this.values = new AtomicReference[size];

            int i;
            for (i = 0; i < size; ++i) {
                this.values[i] = new AtomicReference();
                this.values[i].set(null);
            }

            this.resources = (R[]) new Object[size];
            this.linkedBlockingQueue = new LinkedBlockingQueue(size);

            for (i = 0; i < size; ++i) {
                this.linkedBlockingQueue.add(Integer.valueOf(i));
            }

        }
    }

    public T obtainValue(int resourceId, Class<? extends T> cls) {
        AtomicReference ar = this.values[resourceId];
        Object t = ar.get();
        if (t != null) {
            return (T) t;
        } else {
            Object tt;
            try {
                tt = cls.newInstance();
            } catch (InstantiationException var7) {
                throw new RuntimeException("failed to init!", var7);
            } catch (IllegalAccessException var8) {
                throw new RuntimeException("IllegalAccessException", var8);
            }

            boolean flag = ar.compareAndSet(null, tt);
            return (T) (flag ? tt : ar.get());
        }
    }

    public AtomicReference<T>[] getAllValues() {
        return this.values;
    }

    public int size() {
        Integer i = this.linkedBlockingQueue.peek();
        return i == null ? this.resources.length : i.intValue();
    }

    public Integer registerResource(R res) {
        Integer index = this.linkedBlockingQueue.poll();
        if (index == null) {
            return null;
        } else {
            this.resources[index] = res;
            return index;
        }
    }

    public R getReource(int resourceId) {
        return this.resources[resourceId];
    }
}
