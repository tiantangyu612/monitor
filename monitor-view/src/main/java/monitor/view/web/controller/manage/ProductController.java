package monitor.view.web.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bjlizhitao on 2018/1/16.
 * 产品维护控制器
 */
@Controller
@RequestMapping("/monitor/product")
public class ProductController {
    /**
     * 产品管理
     *
     * @return
     */
    @RequestMapping("/manage")
    public String manage() {
        return "manage/product";
    }

    /**
     * 应用管理
     *
     * @return
     */
    @RequestMapping("/application/manage")
    public String applicationManage() {
        return "manage/application";
    }
}
