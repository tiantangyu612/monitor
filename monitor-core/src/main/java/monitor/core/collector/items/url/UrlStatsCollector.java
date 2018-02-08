package monitor.core.collector.items.url;

import monitor.core.collector.base.AbstractCollector;
import monitor.core.log.MonitorLogFactory;

import java.util.logging.Logger;

public class UrlStatsCollector extends AbstractCollector {
    @Override
    public String getName() {
        return "Url";
    }

    private static Logger LOG = MonitorLogFactory.getLogger(UrlStatsCollector.class);
    private static UrlStatsAggregator urlStatsAggregator = new UrlStatsAggregator();
    private static UrlOverallAggregator urlOverallAggregator = new UrlOverallAggregator();
    private static UrlStatusGroupAggregator urlStatusGroupAggregator = new UrlStatusGroupAggregator();
    public static UrlStatsCollector instance = new UrlStatsCollector();

    private UrlStatsCollector() {
    }

    public static void onStart(String uri, String method) {

        if (uri != null) {
            method = "ALL";

            urlStatsAggregator.onStart(uri, method);
        }
    }

    public static void onStartCollectNativeUrlInfo(String nativeUrl) {
        urlStatsAggregator.onStartCollectNativeUrlInfo(nativeUrl);
    }

    public static void onStart(String uri, String method, String nativeUri) {
        if (uri != null) {
            method = "ALL";

            urlStatsAggregator.onStart(uri, method);
        }
    }

    public static void onThrowable(Throwable th) {
        urlStatsAggregator.onThrowable(th);
    }

    public static void onFinally(int code) {
        long t = urlStatsAggregator.onFinally(code);
        if (urlOverallAggregator.isEnable()) {
            urlOverallAggregator.onTotal(t);
            if (code == 200) {
                urlOverallAggregator.on200();
            } else if (code == 404) {
                urlOverallAggregator.on404();
                urlOverallAggregator.onNot200Count();
            } else if (code == 302) {
                urlOverallAggregator.on302();
                urlOverallAggregator.onNot200Count();
            } else {
                urlOverallAggregator.onNot200Count();
            }

            if (code >= 100 && code < 200) {
                urlOverallAggregator.on1xx();
            } else if (code < 300) {
                urlOverallAggregator.on2xx();
            } else if (code < 400) {
                urlOverallAggregator.on3xx();
            } else if (code < 500) {
                urlOverallAggregator.on4xx();
            } else if (code < 600) {
                urlOverallAggregator.on5xx();
            }

            urlStatusGroupAggregator.onCode(code).getCount().incrementAndGet();
        }

    }

    public static void onFinally() {
        onFinally(200);
    }

    public static void onFinally(Object o) {
        onFinally(200);
    }

    static {
        instance.addModelAggregator(urlStatsAggregator);
        instance.addModelAggregator(urlOverallAggregator);
        instance.addModelAggregator(urlStatusGroupAggregator);
    }
}