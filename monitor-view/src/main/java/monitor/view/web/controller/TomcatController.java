package monitor.view.web.controller;

import monitor.view.web.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lizhitao on 2018/1/14.
 * tomcat 监控信息
 */
@Controller
@RequestMapping(value = "/monitor/tomcat")
public class TomcatController extends BaseController {
    /**
     * tomcat 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/tomcatThread")
    public String tomcat(Model model) {
        buildLineChartData("tomcat", "monitor-view_online_Tomcat_tomcat", model);
        return "jvm/classLoading";
    }
}
