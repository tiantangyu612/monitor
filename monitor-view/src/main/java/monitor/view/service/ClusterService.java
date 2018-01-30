package monitor.view.service;

import monitor.view.domain.entity.Cluster;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/30.
 * 集群管理 Service
 */
public interface ClusterService {
    /**
     * 删除集群
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 新增集群
     *
     * @param cluster
     */
    void insert(Cluster cluster);

    /**
     * 更新集群
     *
     * @param cluster
     * @return
     */
    int update(Cluster cluster);

    /**
     * 查询集群列表
     *
     * @param applicationId
     * @param offset
     * @param limit
     * @return
     */
    List<Cluster> queryByApplicationId(Integer applicationId, Integer offset, Integer limit);

    /**
     * 获取集群数量
     *
     * @param applicationId
     * @return
     */
    int count(Integer applicationId);

    /**
     * 按 id 查询集群信息
     *
     * @param id
     * @return
     */
    Cluster findById(Integer id);
}
