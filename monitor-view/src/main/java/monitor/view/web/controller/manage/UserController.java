package monitor.view.web.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lizhitao on 2018/1/16.
 * 用户管理
 */
@Controller
@RequestMapping("/monitor/user")
public class UserController {
    @RequestMapping("/manage")
    public String manage() {
        return "manage/user";
    }
}
