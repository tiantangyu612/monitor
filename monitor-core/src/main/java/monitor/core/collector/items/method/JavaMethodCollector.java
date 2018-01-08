package monitor.core.collector.items.method;

import monitor.core.collector.base.AbstractCollector;

/**
 * Created by lizhitao on 2018/1/8.
 * java 方法执行信息采集器
 */
public class JavaMethodCollector extends AbstractCollector {
    private static JavaMethodCollector instance = new JavaMethodCollector();

    private static JavaMethodCollectorItem javaMethodCollectorItem = new JavaMethodCollectorItem();

    static {
        instance.addCollectorItem(javaMethodCollectorItem);
    }

    private JavaMethodCollector() {
    }

    public static JavaMethodCollector getInstance() {
        return JavaMethodCollector.instance;
    }

    @Override
    public String getName() {
        return "JavaMethod";
    }
}
