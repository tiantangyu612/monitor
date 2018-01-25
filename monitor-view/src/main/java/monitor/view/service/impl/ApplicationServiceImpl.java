package monitor.view.service.impl;

import monitor.view.dao.mysql.ApplicationDao;
import monitor.view.domain.entity.Application;
import monitor.view.service.ApplicationService;
import monitor.view.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/23.
 * 应用管理 Service
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Resource
    private ApplicationDao applicationDao;
    @Resource
    private ProductService productService;

    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        return applicationDao.deleteById(id);
    }

    /**
     * 新增应用
     *
     * @param application
     */
    @Override
    public void insert(Application application) {
        applicationDao.insert(application);
    }

    /**
     * 更新应用
     *
     * @param application
     * @return
     */
    @Override
    public int update(Application application) {
        return applicationDao.updateById(application);
    }

    /**
     * 查询应用列表
     *
     * @param productId
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Application> queryByProductId(Integer productId, Integer offset, Integer limit) {
        return applicationDao.getApplicationList(productId, offset, limit);
    }

    /**
     * 获取应用数量
     *
     * @param productId
     * @return
     */
    @Override
    public int count(Integer productId) {
        return applicationDao.count(productId);
    }

    /**
     * 按 id 查询应用信息
     *
     * @param id
     * @return
     */
    @Override
    public Application findById(Integer id) {
        return applicationDao.getById(id);
    }
}
