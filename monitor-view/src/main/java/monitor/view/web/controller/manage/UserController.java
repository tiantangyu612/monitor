package monitor.view.web.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lizhitao on 2018/1/16.
 * 用户管理
 */
@Controller
@RequestMapping("/monitor/manage")
public class UserController {
    @RequestMapping("/users")
    public String manage() {
        return "manage/user";
    }
}
