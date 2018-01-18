package monitor.view.dao.mysql;

import monitor.view.domain.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/17.
 * 产品 Dao
 */
public interface ProductDao {
    /**
     * 获取单个产品信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM Product where id = #{id}")
    Product getProductById(Integer id);

    /**
     * 插入产品信息
     *
     * @param product
     * @return
     */
    @Insert("INSERT INTO PRODUCT (id, name, owner, description, createTime) VALUES(#{id},#{name}, #{owner}, #{description}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
//    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    void insert(Product product);

    /**
     * 获取产品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    @Select("SELECT * FROM product ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    List<Product> getProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 更新产品信息
     *
     * @param product
     * @return
     */
    @Update("UPDATE Product SET name=#{name},owner=#{owner},description=#{description} WHERE id=#{id}")
    int updateByProductId(Product product);

    /**
     * 删除产品信息
     *
     * @param id
     * @return
     */
    @Update("DELETE FROM Product WHERE id=#{id}")
    int deleteByProductId(Integer id);
}
