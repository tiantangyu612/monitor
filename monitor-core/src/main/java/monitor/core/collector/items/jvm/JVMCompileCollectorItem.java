package monitor.core.collector.items.jvm;

import monitor.core.collector.base.AbstractCollectorItem;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * JVM 编译信息采集
 */
public class JVMCompileCollectorItem extends AbstractCollectorItem {
    private CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
    private long lastCompilationTime = 0L;

    @Override
    public String getName() {
        return "compile";
    }

    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectData = new ArrayList<Map<String, Object>>(1);

        Map<String, Object> jvmCompilationInfo = new HashMap<String, Object>(2);
        long time = this.compilationMXBean.getTotalCompilationTime();
        jvmCompilationInfo.put("compilationTime", (time - this.lastCompilationTime));
        jvmCompilationInfo.put("totalCompilationTime", time);
        this.lastCompilationTime = time;
        collectData.add(jvmCompilationInfo);

        return collectData;
    }
}
