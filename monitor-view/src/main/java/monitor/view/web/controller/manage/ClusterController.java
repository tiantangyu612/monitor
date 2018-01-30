package monitor.view.web.controller.manage;

import monitor.core.annotation.Monitor;
import monitor.view.service.ApplicationService;
import monitor.view.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by lizhitao on 2018/1/30.
 * 集群管理
 */
@Controller
@RequestMapping("/monitor/manage")
public class ClusterController {
    @Resource
    private ProductService productService;
    @Resource
    private ApplicationService applicationService;

    /**
     * 集群管理
     *
     * @return
     */
    @RequestMapping("/products/{productId}/application/{applicationId}")
    @Monitor
    public String clusterManage(@PathVariable(value = "productId") Integer productId,
                                @PathVariable(value = "applicationId") Integer applicationId) {
        return "manage/cluster/cluster";
    }
}
