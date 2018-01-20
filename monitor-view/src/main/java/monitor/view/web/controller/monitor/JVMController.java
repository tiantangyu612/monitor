package monitor.view.web.controller.monitor;

import monitor.core.annotation.Monitor;
import monitor.view.web.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bjlizhitao on 2018/1/10.
 * HelloController
 */
@Controller
@RequestMapping(value = "/monitor/jvm")
public class JVMController extends BaseController {

    /**
     * 内存信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/memory/{application}/{cluster}")
    @Monitor
    public String memory(@PathVariable("application") String application,
                         @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JVM_memory";
        String sql = "select heapMemoryUsage,nonHeapMemoryUsage from \"" + measurement + "\"";
        buildLineChartData("memory", sql, model);
        return "monitor/jvm/memory";

    }

    /**
     * 类加载信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/classLoading/{application}/{cluster}")
    @Monitor
    public String classLoading(@PathVariable("application") String application,
                               @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JVM_classLoading";
        String sql = "select loadedClassCount,totalLoadedClassCount,unloadedClassCount from \"" + measurement + "\"";
        buildLineChartData("classLoading", sql, model);
        return "monitor/jvm/jvm";
    }

    /**
     * 编译信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/compile/{application}/{cluster}")
    @Monitor
    public String compile(@PathVariable("application") String application,
                          @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JVM_compile";
        String sql = "select compilationTime,totalCompilationTime from \"" + measurement + "\"";
        buildLineChartData("compile", sql, model);
        return "monitor/jvm/jvm";
    }

    /**
     * cpu 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/cpu/{application}/{cluster}")
    @Monitor
    public String cpu(@PathVariable("application") String application,
                      @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JVM_cpu";
        String sql = "select cpuRatio from \"" + measurement + "\"";
        buildLineChartData("cpu", sql, model);
        return "monitor/jvm/jvm";
    }

    /**
     * thread 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/thread/{application}/{cluster}")
    @Monitor
    public String thread(@PathVariable("application") String application,
                         @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JVM_thread";
        String sql = "select daemonThreadCount,deadlockedThreadsCount,peakThreadCount,threadCount,totalStartedThreadCount from \"" + measurement + "\"";
        buildLineChartData("thread", sql, model);
        return "monitor/jvm/jvm";
    }

    /**
     * gc 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/gc/{application}/{cluster}")
    @Monitor
    public String gc(@PathVariable("application") String application,
                     @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JVM_GC";
        String sql = "select fullGCCollectionCount,fullGCCollectionTime,youngGCCollectionCount,youngGCCollectionTime from \"" + measurement + "\"";
        buildLineChartData("gc", sql, model);
        return "monitor/jvm/jvm";
    }

    /**
     * memory pool 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/memoryPool/{application}/{cluster}")
    @Monitor
    public String memoryPool(@PathVariable("application") String application,
                             @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JVM_memoryPool";
        String sql = "select \"Code Cache max\",\"Code Cache used\"," +
                "\"PS Eden Space max\",\"PS Eden Space used\"," +
                "\"PS Survivor Space max\",\"PS Survivor Space used\"," +
                "\"PS Old Gen max\",\"PS Old Gen used\"" +
//                "\"PS Perm Gen max\",\"PS Perm Gen used\"" +
                " from \"" + measurement + "\"";
        buildLineChartData("memoryPool", sql, model);
        return "monitor/jvm/memory";
    }
}
