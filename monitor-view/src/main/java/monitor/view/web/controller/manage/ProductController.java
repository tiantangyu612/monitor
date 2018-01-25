package monitor.view.web.controller.manage;

import monitor.core.annotation.Monitor;
import monitor.view.domain.entity.Product;
import monitor.view.domain.entity.User;
import monitor.view.domain.vo.Pager;
import monitor.view.service.ProductService;
import monitor.view.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private UserService userService;

    /**
     * 产品管理
     *
     * @return
     */
    @RequestMapping("/products")
    @Monitor
    public String manage(Model model, @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage) {
        int count = productService.count();

        Pager<Product> pager = new Pager<Product>(10, count, currentPage);

        List<Product> products = productService.getProductList(pager.getOffset(), pager.getPageSize());
        pager.setDataList(products);
        model.addAttribute("pager", pager);

        List<User> users = userService.getUserList(0, Integer.MAX_VALUE);
        model.addAttribute("users", users);

        return "manage/product/product";
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
     * 修改产品前查询产品信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/products/update/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Product updateProduct(@PathVariable(value = "id") Integer id) {
        return productService.getProductById(id);
    }

    /**
     * 修改产品前查询产品信息
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/products/update", method = RequestMethod.POST)
    public String updateProduct(Product product) {
        productService.update(product);
        return "redirect:/monitor/manage/products";
    }
}
