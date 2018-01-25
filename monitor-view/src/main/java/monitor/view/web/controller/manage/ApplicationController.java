package monitor.view.web.controller.manage;

import monitor.core.annotation.Monitor;
import monitor.view.domain.entity.Application;
import monitor.view.domain.entity.Product;
import monitor.view.domain.vo.Pager;
import monitor.view.service.ApplicationService;
import monitor.view.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/25.
 * 应用管理
 */
@Controller
@RequestMapping("/monitor/manage")
public class ApplicationController {
    @Resource
    private ProductService productService;
    @Resource
    private ApplicationService applicationService;

    /**
     * 应用管理
     *
     * @return
     */
    @RequestMapping("/applications/{productId}")
    @Monitor
    public String applicationManage(Model model, @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                    @PathVariable(value = "productId") Integer productId) {
        int count = applicationService.count(productId);

        Pager<Application> pager = new Pager<Application>(10, count, currentPage);

        List<Application> applications = applicationService.queryByProductId(productId, pager.getOffset(), pager.getPageSize());
        pager.setDataList(applications);
        model.addAttribute("pager", pager);

        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);

        List<Product> products = productService.getProductList(0, Integer.MAX_VALUE);
        model.addAttribute("products", products);

        return "manage/application/application";
    }

    /**
     * 添加应用
     *
     * @return
     */
    @RequestMapping(value = "/applications/add", method = RequestMethod.POST)
    @Monitor
    public String addApplication(Application application) {
        applicationService.insert(application);
        return "redirect:/monitor/manage/applications/" + application.getProductId();
    }

    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/applications/delete/{productId}/{id}")
    @Monitor
    public String deleteApplication(@PathVariable("productId") Integer productId,
                                    @PathVariable("id") Integer id) {
        applicationService.deleteById(id);
        return "redirect:/monitor/manage/applications/" + productId;
    }

    /**
     * 集群管理
     *
     * @return
     */
    @RequestMapping("/products/{productId}/application/{applicationId}")
    @Monitor
    public String clusterManage(@PathVariable(value = "productId") Integer productId,
                                @PathVariable(value = "applicationId") Integer applicationId) {
        return "manage/product/cluster";
    }

    /**
     * 修改应用前查询应用信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/applications/update/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Application updateApplicaiton(@PathVariable(value = "id") Integer id) {
        return applicationService.findById(id);
    }

    /**
     * 修改应用
     *
     * @param application
     * @return
     */
    @RequestMapping(value = "/applications/update", method = RequestMethod.POST)
    public String updateApplication(Application application) {
        applicationService.update(application);
        return "redirect:/monitor/manage/applications/" + application.getProductId();
    }
}
