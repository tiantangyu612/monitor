package monitor.core.collector.items.url;

import com.sun.webpane.platform.ConfigManager;
import monitor.core.collector.base.Collector;

import java.util.regex.MatchResult;

public class UrlStatsCollector extends Collector {
    private static Logger LOG = CollectorLogFactory.getLogger(UrlStatsCollector.class);
    private static UrlPattern pattern = ConfigManager.getUrlPattern();
    private static UrlStatsAggregator urlStatsAggregator = new UrlStatsAggregator();
    private static UrlOverallAggregator urlOverallAggregator = new UrlOverallAggregator();
    private static UrlStatusGroupAggregator urlStatusGroupAggregator = new UrlStatusGroupAggregator();
    private static boolean isDebugEnalble = ConfigManager.isDebugEnable();
    private static boolean isDistinguishHttpMethod = ConfigManager.isDistinguishHttpMethod();
    public static UrlStatsCollector instance = new UrlStatsCollector();

    private UrlStatsCollector() {
    }

    public static MatchResult onMatch(String uri) {
        return pattern == null?null:pattern.matchedUrl(uri);
    }

    public static void onStart(String uri, String method) {
        if(isDebugEnalble) {
            LOG.info("into URI:" + uri + ",method:" + method);
        }

        if(uri != null) {
            if(!isDistinguishHttpMethod) {
                method = "ALL";
            }

            urlStatsAggregator.onStart(uri, method);
        }
    }

    public static void onStartCollectNativeUrlInfo(String nativeUrl) {
        urlStatsAggregator.onStartCollectNativeUrlInfo(nativeUrl);
    }

    public static void onStart(String uri, String method, String nativeUri) {
        if(isDebugEnalble) {
            LOG.info("into URI:" + uri + ",method:" + method);
        }

        if(uri != null) {
            if(!isDistinguishHttpMethod) {
                method = "ALL";
            }

            urlStatsAggregator.onStart(uri, method);
        }
    }

    public static void onThrowable(Throwable th) {
        if(isDebugEnalble) {
            LOG.info("onThrowable:" + th.getClass().getName());
        }

        urlStatsAggregator.onThrowable(th);
    }

    public static void onFinally(int code) {
        if(isDebugEnalble) {
            LOG.info("onFinally:" + code);
        }

        long t = urlStatsAggregator.onFinally(code);
        if(urlOverallAggregator.isEnable()) {
            urlOverallAggregator.onTotal(t);
            if(code == 200) {
                urlOverallAggregator.on200();
            } else if(code == 404) {
                urlOverallAggregator.on404();
                urlOverallAggregator.onNot200Count();
            } else if(code == 302) {
                urlOverallAggregator.on302();
                urlOverallAggregator.onNot200Count();
            } else {
                urlOverallAggregator.onNot200Count();
            }

            if(code >= 100 && code < 200) {
                urlOverallAggregator.on1xx();
            } else if(code < 300) {
                urlOverallAggregator.on2xx();
            } else if(code < 400) {
                urlOverallAggregator.on3xx();
            } else if(code < 500) {
                urlOverallAggregator.on4xx();
            } else if(code < 600) {
                urlOverallAggregator.on5xx();
            }

            urlStatusGroupAggregator.onCode(code).getCount().incrementAndGet();
        }

    }

    public static void onFinally() {
        onFinally(200);
    }

    public static void onFinally(Object o) {
        if(isDebugEnalble) {
            LOG.info("onfinallly,response class:" + o.getClass().getName());
        }

        onFinally(200);
    }

    public String getCollectorName() {
        return ColectorNames.COLLECTOR_URL;
    }

    static {
        instance.addModelAggregator(urlStatsAggregator);
        instance.addModelAggregator(urlOverallAggregator);
        instance.addModelAggregator(urlStatusGroupAggregator);
    }
}