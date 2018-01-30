package monitor.view.web.controller.manage;

import monitor.core.annotation.Monitor;
import monitor.view.domain.entity.Application;
import monitor.view.domain.entity.Cluster;
import monitor.view.domain.vo.Pager;
import monitor.view.service.ApplicationService;
import monitor.view.service.ClusterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/30.
 * 集群管理
 */
@Controller
@RequestMapping("/monitor/manage")
public class ClusterController {
    @Resource
    private ApplicationService applicationService;
    @Resource
    private ClusterService clusterService;

    /**
     * 集群管理
     *
     * @param model
     * @param currentPage
     * @param productId
     * @param applicationId
     * @return
     */
    @RequestMapping("/clusters/{productId}/{applicationId}")
    @Monitor
    public String clusterManage(Model model,
                                @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                @PathVariable(value = "productId") Integer productId,
                                @PathVariable(value = "applicationId") Integer applicationId) {
        List<Application> applications = applicationService.queryByProductId(productId, 0, Integer.MAX_VALUE);
        model.addAttribute("applications", applications);


        int count = clusterService.count(productId);

        Pager<Cluster> pager = new Pager<Cluster>(10, count, currentPage);

        List<Cluster> clusters = clusterService.queryByApplicationId(applicationId, pager.getOffset(), pager.getPageSize());
        pager.setDataList(clusters);
        model.addAttribute("pager", pager);

        Application application = applicationService.findById(applicationId);
        model.addAttribute("application", application);

        return "manage/cluster/cluster";
    }

    /**
     * 添加集群
     *
     * @param cluster
     * @return
     */
    @RequestMapping(value = "/clusters/add", method = RequestMethod.POST)
    @Monitor
    public String addCluster(Cluster cluster) {
        clusterService.insert(cluster);
        return "redirect:/monitor/manage/clusters/" + cluster.getProductId() + "/" + cluster.getApplicationId();
    }

    /**
     * 删除集群
     *
     * @param productId
     * @param applicationId
     * @param id
     * @return
     */
    @RequestMapping(value = "/clusters/delete/{productId}/{applicationId}/{id}")
    @Monitor
    public String deleteCluster(@PathVariable("productId") Integer productId,
                                @PathVariable("applicationId") Integer applicationId,
                                @PathVariable("id") Integer id) {
        clusterService.deleteById(id);
        return "redirect:/monitor/manage/clusters/" + productId + "/" + applicationId;
    }

    /**
     * 修改集群前查询集群信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/clusters/update/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Cluster updateCluster(@PathVariable(value = "id") Integer id) {
        return clusterService.findById(id);
    }

    /**
     * 修改应用
     *
     * @param cluster
     * @return
     */
    @RequestMapping(value = "/clusters/update", method = RequestMethod.POST)
    public String updateCluster(Cluster cluster) {
        clusterService.update(cluster);
        return "redirect:/monitor/manage/clusters/" + cluster.getProductId() + "/" + cluster.getApplicationId();
    }
}
