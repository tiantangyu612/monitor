//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package monitor.core.collector.items.url;

import com.netease.sentry.javaagent.collector.CollectorLogFactory;
import com.netease.sentry.javaagent.collector.api.MultiPrimaryKeyAggregator;
import com.netease.sentry.javaagent.collector.api.PrimaryKey;
import com.netease.sentry.javaagent.collector.plugin.url.UrlStats;
import com.netease.sentry.javaagent.collector.stats.SectionStatsValue;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UrlStatsAggregator extends MultiPrimaryKeyAggregator<UrlStats> {
    private static Logger LOG = CollectorLogFactory.getLogger(UrlStatsAggregator.class);
    private static ThreadLocal<UrlStats> statsLocal = new ThreadLocal();
    private static ThreadLocal<Long> timeLocal = new ThreadLocal();
    private static ThreadLocal<PrimaryKey> pkLocal = new ThreadLocal();
    private static ThreadLocal<String> nativeUrlLocal = new ThreadLocal();

    public UrlStatsAggregator() {
        ClassLoader cl = this.getClass().getClassLoader();
        LOG.info("url classloader:" + (cl == null?"null":cl.getClass().getName()));
    }

    protected Class<UrlStats> getValueType() {
        return UrlStats.class;
    }

    protected int primaryKeyLength() {
        return 2;
    }

    public String getModelName() {
        return "url";
    }

    protected Map<String, Object> constructItemRow(PrimaryKey key, UrlStats value) {
        SectionStatsValue sv = value.harvest();
        if(sv == null) {
            return null;
        } else {
            HashMap row = new HashMap(15);
            row.put("url", key.get(0));
            row.put("method", key.get(1));
            row.put("runningCount", Integer.valueOf(sv.getRunningCount()));
            row.put("invokeCount", Integer.valueOf(sv.getInvokeCount()));
            row.put("totalTime", Integer.valueOf(sv.getTotalTime()));
            row.put("concurrentMax", Integer.valueOf(sv.getConcurrentMax()));
            row.put("maxTime", Integer.valueOf(sv.getMaxTime()));
            row.put("errorCount", Integer.valueOf(sv.getErrorCount()));
            row.put("lastError", sv.getLastError());
            row.put("ms0_10", Integer.valueOf(sv.getMs0_10()));
            row.put("ms10_100", Integer.valueOf(sv.getMs10_100()));
            row.put("ms100_1000", Integer.valueOf(sv.getMs100_1000()));
            row.put("s1_10", Integer.valueOf(sv.getS1_10()));
            row.put("s10_n", Integer.valueOf(sv.getS10_n()));
            row.put("maxTimeNativeUrl", value.harvestMaxTimeNativeUrl());
            return row;
        }
    }

    public void onStart(String url, String method) {
        if(this.isEnable) {
            PrimaryKey pk = new PrimaryKey(new String[]{url, method});
            UrlStats stats = (UrlStats)this.getValue(pk);
            long start = stats.onStart();
            statsLocal.set(stats);
            timeLocal.set(Long.valueOf(start));
            pkLocal.set(pk);
        }
    }

    public void onStartCollectNativeUrlInfo(String nativeUrl) {
        if(this.isEnable) {
            nativeUrlLocal.set(nativeUrl);
        }
    }

    public void onThrowable(Throwable th) {
        if(this.isEnable) {
            UrlStats stat = (UrlStats)statsLocal.get();
            if(stat != null) {
                stat.onThrowable(th);
            }

        }
    }

    public long onFinally(int code) {
        if(!this.isEnable) {
            return 0L;
        } else {
            UrlStats stat = (UrlStats)statsLocal.get();
            Long t = (Long)timeLocal.get();
            String nativeUrl = (String)nativeUrlLocal.get();
            long used = 0L;
            if(stat != null && t != null) {
                used = stat.onFinally(t.longValue(), nativeUrl);
                statsLocal.set((Object)null);
                timeLocal.set((Object)null);
            }

            if(code != 200 && code != 302) {
                PrimaryKey pk = (PrimaryKey)pkLocal.get();
                if(pk != null) {
                    this.removeValue(pk);
                }
            }

            nativeUrlLocal.remove();
            pkLocal.set((Object)null);
            return used;
        }
    }
}
