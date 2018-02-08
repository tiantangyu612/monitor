//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package monitor.core.collector.items.url;

import com.netease.sentry.javaagent.collector.api.SinglePrimaryKeyAggregator;
import com.netease.sentry.javaagent.collector.plugin.url.StatusCodeStats;
import java.util.HashMap;
import java.util.Map;

public class UrlStatusGroupAggregator extends SinglePrimaryKeyAggregator<StatusCodeStats> {
    public UrlStatusGroupAggregator() {
    }

    public String getModelName() {
        return "statuscode";
    }

    protected Map<String, Object> constructItemRow(String key, StatusCodeStats value) {
        HashMap m = new HashMap(2);
        m.put("code", key);
        m.put("count", Long.valueOf(value.getCount().getAndSet(0L)));
        return m;
    }

    protected Class<StatusCodeStats> getValueType() {
        return StatusCodeStats.class;
    }

    public StatusCodeStats onCode(int code) {
        return (StatusCodeStats)this.getValue(code + "");
    }
}
