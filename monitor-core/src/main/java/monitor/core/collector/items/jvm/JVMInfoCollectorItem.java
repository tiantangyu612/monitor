package monitor.core.collector.items.jvm;

import monitor.core.collector.base.OneRowCollectorItem;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/9.
 * jvm info 采集项
 */
public class JVMInfoCollectorItem extends OneRowCollectorItem {
    private OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    private RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

    @Override
    public String getName() {
        return "info";
    }

    @Override
    protected Map<String, Object> collectItemData() {
        Map<String, Object> jvmInfo = new HashMap<String, Object>();
        jvmInfo.put("arch", this.operatingSystemMXBean.getArch());
        jvmInfo.put("osName", this.operatingSystemMXBean.getName());
        jvmInfo.put("osVersion", this.operatingSystemMXBean.getVersion());
        jvmInfo.put("systemLoadAverage", this.operatingSystemMXBean.getSystemLoadAverage());
        jvmInfo.put("availableProcessors", this.operatingSystemMXBean.getAvailableProcessors());
        jvmInfo.put("startTime", this.runtimeMXBean.getStartTime());
        jvmInfo.put("inputArguments", this.runtimeMXBean.getInputArguments().toString());
        jvmInfo.put("javaSpecificationVersion", this.runtimeMXBean.getManagementSpecVersion());
        jvmInfo.put("JavaLibraryPath", this.runtimeMXBean.getLibraryPath());
        jvmInfo.put("bootClassPath", this.runtimeMXBean.getBootClassPath());
        jvmInfo.put("classPath", this.runtimeMXBean.getClassPath());
        jvmInfo.put("name", this.runtimeMXBean.getName());
        jvmInfo.put("vmName", this.runtimeMXBean.getVmName());
        jvmInfo.put("vmVendor", this.runtimeMXBean.getVmVendor());
        jvmInfo.put("vmVersion", this.runtimeMXBean.getVmVersion());
        jvmInfo.put("specName", this.runtimeMXBean.getSpecName());
        jvmInfo.put("specVendor", this.runtimeMXBean.getSpecVendor());
        jvmInfo.put("specVersion", this.runtimeMXBean.getSpecVersion());
        jvmInfo.put("uptime", this.runtimeMXBean.getUptime());
        jvmInfo.put("jvm", System.getProperties().get("java.vm.info"));
        jvmInfo.put("javaVersion", System.getProperties().get("java.version"));
        String name = this.runtimeMXBean.getName();
        String pid = "";
        if (name != null) {
            int home = name.indexOf("@");
            if (home > 1) {
                pid = name.substring(0, home);
            } else {
                pid = name;
            }
        }

        jvmInfo.put("pid", pid);
        String home1 = (String) System.getProperties().get("java.home");
        if (home1 != null) {
            jvmInfo.put("javaHome", home1);
        }

        String encodeing = (String) System.getProperties().get("file.encoding");
        if (encodeing != null) {
            jvmInfo.put("fileEncode", encodeing);
        }

        return jvmInfo;
    }
}
