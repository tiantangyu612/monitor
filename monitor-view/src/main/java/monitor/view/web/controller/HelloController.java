package monitor.view.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bjlizhitao on 2018/1/10.
 * HelloController
 */
@Controller
public class HelloController {
    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "hello";
    }
}
