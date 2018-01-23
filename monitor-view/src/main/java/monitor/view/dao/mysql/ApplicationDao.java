package monitor.view.dao.mysql;

import monitor.view.domain.entity.Application;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/23.
 * 应用操作 dao
 */
public interface ApplicationDao {
    /**
     * 获取单个应用信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM Application where id = #{id}")
    Application getById(Integer id);

    /**
     * 查询应用总数
     *
     * @return
     */
    @Select("SELECT COUNT(*) FROM Application where productId=?")
    int count(@Param("productId") Integer productId);

    /**
     * 插入应用信息
     *
     * @param application
     * @return
     */
    @Insert("INSERT INTO Application (id, name, productId, description, owner, createTime) VALUES(#{id},#{name}, #{productId}, #{description}, #{owner}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Application application);

    /**
     * 获取应用列表
     *
     * @param productId
     * @param offset
     * @param limit
     * @return
     */
    @Select("SELECT * FROM Application WHERE productId=? ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    List<Application> getProductList(@Param("productId") Integer productId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 更新应用信息
     *
     * @param application
     * @return
     */
    @Update("UPDATE Application SET name=#{name},productId=#{productId}, owner=#{owner},description=#{description} WHERE id=#{id}")
    int updateById(Application application);

    /**
     * 删除应用信息
     *
     * @param id
     * @return
     */
    @Update("DELETE FROM Application WHERE id=#{id}")
    int deleteById(Integer id);
}
