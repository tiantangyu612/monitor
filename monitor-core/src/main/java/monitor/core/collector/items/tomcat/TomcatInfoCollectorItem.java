package monitor.core.collector.items.tomcat;

import monitor.core.collector.base.AbstractCollectorItem;
import monitor.core.log.MonitorLogFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bjlizhitao on 2018/1/10.
 * tomcat 相关信息数据项采集
 */
public class TomcatInfoCollectorItem extends AbstractCollectorItem {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(TomcatInfoCollectorItem.class);
    MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
    private AtomicInteger failCount = new AtomicInteger(0);
    private Map<String, Object> tomcatInfo = new ConcurrentHashMap<String, Object>();
    private volatile boolean obtainSuccess = false;

    @Override
    public String getName() {
        return "tomcatInfo";
    }

    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectDatas = new ArrayList<Map<String, Object>>();
        if (this.obtainSuccess) {
            collectDatas.add(tomcatInfo);
        } else if (this.failCount.get() >= 8) {
            return collectDatas;
        } else {
            this.obtainVersion();
            collectDatas.add(tomcatInfo);
        }

        return collectDatas;
    }

    private boolean obtainVersion() {
        if (!this.obtainSuccess && this.failCount.get() < 8) {
            try {
                ObjectName serverObject = new ObjectName("Catalina:type=Server");
                Object serverInfo = this.platformMBeanServer.getAttribute(serverObject, "serverInfo");
                if (null != serverInfo) {
                    String serverVersion = serverInfo.toString();
                    this.tomcatInfo.put("tomcatVersion", serverVersion);
                    this.obtainSuccess = true;
                }
            } catch (Throwable e) {
                this.failCount.incrementAndGet();
                LOGGER.log(Level.SEVERE, "failed to obtain tomcat platformMBeanServer version, " + e.getMessage(), e);
            }
            return this.obtainSuccess;
        } else {
            return this.obtainSuccess;
        }
    }
}