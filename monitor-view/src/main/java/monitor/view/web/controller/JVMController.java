package monitor.view.web.controller;

import monitor.view.web.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bjlizhitao on 2018/1/10.
 * HelloController
 */
@Controller
@RequestMapping(value = "/monitor/jvm")
public class JVMController extends BaseController{

    /**
     * 内存信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/memory")
    public String memory(Model model) {
        buildLineChartData("memory", "monitor-view_online_JVM_memory", model);
        return "jvm/memory";

    }

    /**
     * 类加载信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/classLoading")
    public String classLoading(Model model) {
        buildLineChartData("classLoading", "monitor-view_online_JVM_classLoading", model);
        return "jvm/classLoading";
    }

    /**
     * 编译信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/compile")
    public String compile(Model model) {
        buildLineChartData("compile", "monitor-view_online_JVM_compile", model);
        return "jvm/classLoading";
    }

    /**
     * cpu 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/cpu")
    public String cpu(Model model) {
        buildLineChartData("cpu", "monitor-view_online_JVM_cpu", model);
        return "jvm/classLoading";
    }

    /**
     * thread 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/thread")
    public String thread(Model model) {
        buildLineChartData("thread", "monitor-view_online_JVM_thread", model);
        return "jvm/classLoading";
    }

    /**
     * gc 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/gc")
    public String gc(Model model) {
        buildLineChartData("gc", "monitor-view_online_JVM_GC", model);
        return "jvm/classLoading";
    }

    /**
     * memory pool 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/memoryPool")
    public String memoryPool(Model model) {
        buildLineChartData("memoryPool", "monitor-view_online_JVM_memoryPool", model);
        return "jvm/memory";
    }
}
