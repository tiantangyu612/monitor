package monitor.view.web.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lizhitao on 2018/1/17.
 * 监控配置管理
 */
@Controller
@RequestMapping("/monitor/config")
public class MonitorConfigController {
    /**
     * 采集器配置
     *
     * @return
     */
    @RequestMapping("/collector")
    public String collector() {
        return "manage/monitor/collector";
    }

    /**
     * 基础监控配置
     *
     * @return
     */
    @RequestMapping("/basic")
    public String basic() {
        return "manage/monitor/basic";
    }
}
