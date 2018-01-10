package monitor.core.collector.items.tomcat;

import monitor.core.collector.base.AbstractCollector;

/**
 * Created by lizhitao on 2018/1/9.
 * tomcat 采集器
 */
public class TomcatCollector extends AbstractCollector {
    private static TomcatCollector instance = new TomcatCollector();
    private static TomcatCollectorItem tomcatCollectorItem = new TomcatCollectorItem();

    static {
        instance.addCollectorItem(tomcatCollectorItem);
    }

    public static TomcatCollector getInstance() {
        return TomcatCollector.instance;
    }

    @Override
    public String getName() {
        return "Tomcat";
    }
}
