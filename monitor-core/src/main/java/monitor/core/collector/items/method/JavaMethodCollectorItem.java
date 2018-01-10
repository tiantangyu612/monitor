package monitor.core.collector.items.method;

import monitor.core.collector.base.AbstractCollectorItem;
import monitor.core.config.MonitorConfig;
import monitor.core.util.concurrent.ConcurrentResourceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lizhitao on 2018/1/8.
 * java method 采集项
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
            List<Map<String, Object>> collectDatas = new ArrayList<Map<String, Object>>();
            AtomicReference[] javaMethodCollectInfos = RESOURCE_FACTORY.getAllValues();

            for (int resourceId = 0; resourceId < size; ++resourceId) {
                JavaMethodCollectInfo javaMethodCollectInfo = (JavaMethodCollectInfo) javaMethodCollectInfos[resourceId].get();
                if (javaMethodCollectInfo != null) {
                    Map<String, Object> collectData = javaMethodCollectInfo.collectData();
                    if (collectData != null) {
                        ClassAndMethod classAndMethod = RESOURCE_FACTORY.getResource(resourceId);
                        collectData.put("class", classAndMethod.getClassName());
                        collectData.put("method", classAndMethod.getMethodName());
                        collectDatas.add(collectData);
                    }
                }
            }

            return collectDatas;
        }
    }

    /**
     * 采集器启动时调用
     *
     * @param resourceId
     * @return
     */
    public JavaMethodCollectInfo onStart(int resourceId) {
        JavaMethodCollectInfo javaMethodCollectInfo = RESOURCE_FACTORY.obtainValue(resourceId, JavaMethodCollectInfo.class);
        javaMethodCollectInfo.onStart();
        return javaMethodCollectInfo;
    }
}
