package monitor.view.web.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bjlizhitao on 2018/1/16.
 * 产品维护控制器
 */
@Controller
@RequestMapping("/monitor/manage")
public class ProductController {
    /**
     * 产品管理
     *
     * @return
     */
    @RequestMapping("/products")
    public String manage() {
        return "manage/product";
    }

    /**
     * 应用管理
     *
     * @return
     */
    @RequestMapping("/products/{productId}")
    public String applicationManage(@PathVariable(value = "productId") Integer productId) {
        return "manage/application";
    }

    /**
     * 集群管理
     *
     * @return
     */
    @RequestMapping("/products/{productId}/application/{applicationId}")
    public String clusterManage(@PathVariable(value = "productId") Integer productId,
                                @PathVariable(value = "applicationId") Integer applicationId) {
        return "manage/cluster";
    }
}
