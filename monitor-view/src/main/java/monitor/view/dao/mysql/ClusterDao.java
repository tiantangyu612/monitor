package monitor.view.dao.mysql;

import monitor.view.domain.entity.Application;
import monitor.view.domain.entity.Cluster;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/25.
 * 集群 dao
 */
public interface ClusterDao {
    /**
     * 获取单个集群信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM Cluster where id = #{id}")
    Cluster getById(Integer id);

    /**
     * 查询应用总数
     *
     * @param applicationId
     * @return
     */
    @Select("SELECT COUNT(*) FROM Cluster where applicationId=?")
    int count(@Param("applicationId") Integer applicationId);

    /**
     * 插入集群信息
     *
     * @param cluster
     * @return
     */
    @Insert("INSERT INTO Cluster (id, name, applicationId, description, alarmUser, alarmGroup, createTime) " +
            "VALUES(#{id},#{name}, #{applicationId}, #{description}, #{alarmUser}, #{alarmGroup}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Cluster cluster);

    /**
     * 获取集群列表
     *
     * @param applicationId
     * @param offset
     * @param limit
     * @return
     */
    @Select("SELECT * FROM Cluster WHERE applicationId=? ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    List<Application> getClusterList(@Param("applicationId") Integer applicationId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 更新集群信息
     *
     * @param cluster
     * @return
     */
    @Update("UPDATE Cluster SET name=#{name},applicationId=#{applicationId},alarmUser=#{alarmUser},alarmGroup=#{alarmGroup},description=#{description} WHERE id=#{id}")
    int updateById(Cluster cluster);

    /**
     * 删除集群信息
     *
     * @param id
     * @return
     */
    @Update("DELETE FROM Cluster WHERE id=#{id}")
    int deleteById(Integer id);
}
