package monitor.core.collector.items.method;

import monitor.core.collector.base.AbstractCollectorItem;
import monitor.core.config.MonitorConfig;
import monitor.core.util.ConcurrentResourceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lizhitao on 2018/1/8.
 */
public class JavaMethodCollectorItem extends AbstractCollectorItem {
    public static ConcurrentResourceFactory<JavaMethodCollectInfo, ClassAndMethod> RESOURCE_FACTORY =
            new ConcurrentResourceFactory<JavaMethodCollectInfo, ClassAndMethod>(MonitorConfig.getMaxCollectJavaMethodCount());

    @Override
    public String getName() {
        return "method";
    }

    @Override
    public List<Map<String, Object>> collectData() {
        int size = RESOURCE_FACTORY.size();
        if (size == 0) {
            return Collections.emptyList();
        } else {
            ArrayList list = new ArrayList();
            AtomicReference[] allValues = RESOURCE_FACTORY.getAllValues();

            for (int resourceId = 0; resourceId < size; ++resourceId) {
                JavaMethodCollectInfo stats = (JavaMethodCollectInfo) allValues[resourceId].get();
                if (stats != null) {
                    Map vv = stats.harvest();
                    if (vv != null) {
                        ClassAndMethod cam = RESOURCE_FACTORY.getReource(resourceId);
                        vv.put("class", cam.getClassName());
                        vv.put("method", cam.getMethodName());
                        list.add(vv);
                    }
                }
            }

            return list;
        }
    }

    public JavaMethodCollectInfo onStart(int resourceId) {
        JavaMethodCollectInfo stats = RESOURCE_FACTORY.obtainValue(resourceId, JavaMethodCollectInfo.class);
        stats.onStart();
        return stats;
    }
}
