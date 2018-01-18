package monitor.view.web.controller.manage;

import monitor.core.annotation.Monitor;
import monitor.view.domain.entity.Product;
import monitor.view.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by bjlizhitao on 2018/1/16.
 * 产品维护控制器
 */
@Controller
@RequestMapping("/monitor/manage")
public class ProductController {
    @Resource
    private ProductService productService;

    /**
     * 产品管理
     *
     * @return
     */
    @RequestMapping("/products")
    @Monitor
    public String manage(Model model, @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                         @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Product> products = productService.getProductList(offset, limit);
        model.addAttribute("products", products);
        return "manage/product";
    }

    /**
     * 添加产品
     *
     * @return
     */
    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    @Monitor
    public String addProduct(Product product) {
        productService.insert(product);
        return "redirect:/monitor/manage/products";
    }

    /**
     * 删除产品
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/products/delete/{id}")
    @Monitor
    public String deleteProduct(@PathVariable(value = "id") Integer id) {
        productService.deleteById(id);
        return "redirect:/monitor/manage/products";
    }

    /**
     * 应用管理
     *
     * @return
     */
    @RequestMapping("/products/{productId}")
    @Monitor
    public String applicationManage(@PathVariable(value = "productId") Integer productId) {
        return "manage/application";
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
        return "manage/cluster";
    }
}
