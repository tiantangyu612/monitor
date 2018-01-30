package monitor.view.service.impl;

import monitor.view.dao.mysql.ClusterDao;
import monitor.view.domain.entity.Cluster;
import monitor.view.service.ClusterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/30.
 * 集群管理 Service
 */
@Service
public class ClusterServiceImpl implements ClusterService {
    @Resource
    private ClusterDao clusterDao;

    /**
     * 删除集群
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        return clusterDao.deleteById(id);
    }

    /**
     * 新增集群
     *
     * @param cluster
     */
    @Override
    public void insert(Cluster cluster) {
        clusterDao.insert(cluster);
    }

    /**
     * 更新集群
     *
     * @param cluster
     * @return
     */
    @Override
    public int update(Cluster cluster) {
        return clusterDao.updateById(cluster);
    }

    /**
     * 查询集群列表
     *
     * @param applicationId
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Cluster> queryByApplicationId(Integer applicationId, Integer offset, Integer limit) {
        return clusterDao.getClusterList(applicationId, offset, limit);
    }

    /**
     * 获取集群数量
     *
     * @param applicationId
     * @return
     */
    @Override
    public int count(Integer applicationId) {
        return clusterDao.count(applicationId);
    }

    /**
     * 按 id 查询集群信息
     *
     * @param id
     * @return
     */
    @Override
    public Cluster findById(Integer id) {
        return clusterDao.getById(id);
    }
}
