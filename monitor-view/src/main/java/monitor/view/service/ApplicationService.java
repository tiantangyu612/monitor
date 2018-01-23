package monitor.view.service;

import monitor.view.domain.entity.Application;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/23.
 * 应用管理 Service
 */
public interface ApplicationService {
    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 新增应用
     *
     * @param application
     */
    void insert(Application application);

    /**
     * 更新应用
     *
     * @param application
     * @return
     */
    int update(Application application);

    /**
     * 查询应用列表
     *
     * @param productId
     * @param offset
     * @param limit
     * @return
     */
    List<Application> queryByProductId(Integer productId, Integer offset, Integer limit);

    /**
     * 获取应用数量
     *
     * @param productId
     * @return
     */
    int count(Integer productId);
}
