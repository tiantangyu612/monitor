package monitor.core.collector.items.tomcat;

import monitor.core.collector.base.AbstractCollectorItem;
import monitor.core.log.MonitorLogFactory;
import monitor.core.util.CollectionUtils;
import monitor.core.util.concurrent.ConcurrentUtil;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bjlizhitao on 2018/1/10.
 * tomcat 数据项采集
 */
public class TomcatCollectorItem extends AbstractCollectorItem {
    private static Logger LOGGER = MonitorLogFactory.getLogger(TomcatCollectorItem.class);
    private MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    private int isNullTimes = 0;
    private ObjectName httpObjectName = null;
    private AtomicInteger currentThreadsBusyMax = new AtomicInteger(0);

    @Override
    public String getName() {
        return "tomcat";
    }

    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectDatas = new ArrayList<Map<String, Object>>();

        if (this.isNullTimes > 3) {
            return collectDatas;
        } else {
            try {
                if (this.httpObjectName == null) {
                    ObjectName catalinaThreadPoolObjectName = new ObjectName("Catalina:type=ThreadPool,name=*");
                    Set<ObjectName> catalinaThreadPoolObjectNames = this.mBeanServer.queryNames(catalinaThreadPoolObjectName, null);
                    if (CollectionUtils.isEmpty(catalinaThreadPoolObjectNames)) {
                        ++this.isNullTimes;
                        LOGGER.log(Level.WARNING, "platformMBeanServer [Catalina:type=ThreadPool,name=*] not found, it may be not a tomcat container ");
                        return collectDatas;
                    }

                    for (ObjectName catalinaObjectName : catalinaThreadPoolObjectNames) {
                        LOGGER.log(Level.WARNING, "getKeyPropertyList:" + catalinaObjectName.getKeyPropertyList());
                        String name = catalinaObjectName.getKeyProperty("name");
                        if (name != null && name.contains("http")) {
                            this.httpObjectName = catalinaObjectName;
                            break;
                        }
                    }

                    if (this.httpObjectName == null) {
                        ++this.isNullTimes;
                        return collectDatas;
                    }
                }

                Map<String, Object> tomcatThreadPoolInfo = new HashMap<String, Object>();
                Object currentThreadsBusyObject = this.mBeanServer.getAttribute(this.httpObjectName, "currentThreadsBusy");
                if (currentThreadsBusyObject != null) {
                    int currentThreadsBusy = Integer.parseInt(currentThreadsBusyObject.toString());
                    tomcatThreadPoolInfo.put("currentThreadsBusy", currentThreadsBusy);
                    int threadBusyMax = this.currentThreadsBusyMax.getAndSet(0);
                    if (currentThreadsBusy > threadBusyMax) {
                        tomcatThreadPoolInfo.put("currentThreadsBusyMax", currentThreadsBusy);
                    } else {
                        tomcatThreadPoolInfo.put("currentThreadsBusyMax", threadBusyMax);
                    }
                }

                currentThreadsBusyObject = this.mBeanServer.getAttribute(this.httpObjectName, "currentThreadCount");
                if (currentThreadsBusyObject != null) {
                    tomcatThreadPoolInfo.put("currentThreadCount", Integer.parseInt(currentThreadsBusyObject.toString()));
                }

                currentThreadsBusyObject = this.mBeanServer.getAttribute(this.httpObjectName, "maxThreads");
                if (currentThreadsBusyObject != null) {
                    tomcatThreadPoolInfo.put("maxThreads", Integer.parseInt(currentThreadsBusyObject.toString()));
                }

                if (tomcatThreadPoolInfo.isEmpty()) {
                    LOGGER.log(Level.WARNING, "no tomcat data collected");
                }

                collectDatas.add(tomcatThreadPoolInfo);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "failed to constructItemRow", e);
                return collectDatas;
            }
        }
        return collectDatas;
    }

    public void update() {
        if (this.httpObjectName != null) {
            try {
                Object currentThreadsBusyObject = this.mBeanServer.getAttribute(this.httpObjectName, "currentThreadsBusy");
                if (currentThreadsBusyObject != null) {
                    int currentThreadsBusy = Integer.parseInt(currentThreadsBusyObject.toString());
                    ConcurrentUtil.setMaxValue(this.currentThreadsBusyMax, currentThreadsBusy);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "failed to update", e);
            }
        }
    }
}
