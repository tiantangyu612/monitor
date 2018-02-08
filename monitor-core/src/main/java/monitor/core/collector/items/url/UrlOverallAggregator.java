//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package monitor.core.collector.items.url;

import com.netease.sentry.javaagent.collector.api.NonePrimaryKeyAggregator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class UrlOverallAggregator extends NonePrimaryKeyAggregator {
    private AtomicLong totalRequestCount = new AtomicLong(0L);
    private AtomicLong totalRequestTime = new AtomicLong(0L);
    private AtomicLong total200Count = new AtomicLong(0L);
    private AtomicLong total404Count = new AtomicLong(0L);
    private AtomicLong total302Count = new AtomicLong(0L);
    private AtomicLong totalNot200Count = new AtomicLong(0L);
    private AtomicLong total1xxCount = new AtomicLong();
    private AtomicLong total2xxCount = new AtomicLong();
    private AtomicLong total3xxCount = new AtomicLong();
    private AtomicLong total4xxCount = new AtomicLong();
    private AtomicLong total5xxCount = new AtomicLong();

    public UrlOverallAggregator() {
    }

    public String getModelName() {
        return "overall";
    }

    public void onTotal(long t) {
        this.totalRequestCount.incrementAndGet();
        this.totalRequestTime.addAndGet(t);
    }

    public void on404() {
        this.total404Count.incrementAndGet();
    }

    public void on302() {
        this.total302Count.incrementAndGet();
    }

    public void onNot200Count() {
        this.totalNot200Count.incrementAndGet();
    }

    public void on200() {
        this.total200Count.incrementAndGet();
    }

    public void on1xx() {
        this.total1xxCount.incrementAndGet();
    }

    public void on2xx() {
        this.total2xxCount.incrementAndGet();
    }

    public void on3xx() {
        this.total3xxCount.incrementAndGet();
    }

    public void on4xx() {
        this.total4xxCount.incrementAndGet();
    }

    public void on5xx() {
        this.total5xxCount.incrementAndGet();
    }

    public Map<String, Object> constructItemRow() {
        long c = this.totalRequestCount.getAndSet(0L);
        if(c > 0L) {
            HashMap row = new HashMap();
            row.put("totalRequestCount", Long.valueOf(c));
            row.put("totalRequestTime", Long.valueOf(this.totalRequestTime.getAndSet(0L)));
            row.put("total200Count", Long.valueOf(this.total200Count.getAndSet(0L)));
            row.put("total404Count", Long.valueOf(this.total404Count.getAndSet(0L)));
            row.put("total302Count", Long.valueOf(this.total302Count.getAndSet(0L)));
            row.put("totalNot200Count", Long.valueOf(this.totalNot200Count.getAndSet(0L)));
            row.put("total1xxCount", Long.valueOf(this.total1xxCount.getAndSet(0L)));
            row.put("total2xxCount", Long.valueOf(this.total2xxCount.getAndSet(0L)));
            row.put("total3xxCount", Long.valueOf(this.total3xxCount.getAndSet(0L)));
            row.put("total4xxCount", Long.valueOf(this.total4xxCount.getAndSet(0L)));
            row.put("total5xxCount", Long.valueOf(this.total5xxCount.getAndSet(0L)));
            return row;
        } else {
            return null;
        }
    }
}
