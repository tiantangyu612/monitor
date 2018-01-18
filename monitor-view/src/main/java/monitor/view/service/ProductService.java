package monitor.view.service;

import monitor.view.domain.entity.Product;

import java.util.List;

/**
 * Created by bjlizhitao on 2018/1/18.
 * 产品管理 Service
 */
public interface ProductService {
    /**
     * 修改产品信息
     *
     * @param product
     */
    int update(Product product);

    /**
     * 删除产品
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 查询产品总数
     *
     * @return
     */
    int count();

    /**
     * 插入产品信息
     *
     * @param product
     * @return
     */
    Product insert(Product product);

    /**
     * 根据 id 获取产品信息
     *
     * @param id
     * @return
     */
    Product getProductById(Integer id);

    /**
     * 获取产品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Product> getProductList(Integer offset, Integer limit);
}
