package monitor.view.service.impl;

import monitor.view.dao.mysql.ProductDao;
import monitor.view.domain.entity.Product;
import monitor.view.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by bjlizhitao on 2018/1/18.
 * 产品管理 Service
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;

    /**
     * 修改产品信息
     *
     * @param product
     */
    @Override
    public int update(Product product) {
        return productDao.updateByProductId(product);
    }

    /**
     * 删除产品
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        return productDao.deleteByProductId(id);
    }

    /**
     * 查询产品总数
     *
     * @return
     */
    @Override
    public int count() {
        return productDao.count();
    }

    /**
     * 插入产品信息
     *
     * @param product
     * @return
     */
    @Override
    public Product insert(Product product) {
        productDao.insert(product);
        return product;
    }

    /**
     * 根据 id 获取产品信息
     *
     * @param id
     * @return
     */
    @Override
    public Product getProductById(Integer id) {
        return productDao.getProductById(id);
    }

    /**
     * 获取产品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Product> getProductList(Integer offset, Integer limit) {
        return productDao.getProductList(offset, limit);
    }
}
