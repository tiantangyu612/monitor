package monitor.view.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lizhitao on 2018/1/14.
 * index controller
 */
@Controller
public class IndexController {
    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    /**
     * 主页面
     *
     * @return
     */
    @RequestMapping(value = "/main")
    public String main() {
        return "main";
    }
}
