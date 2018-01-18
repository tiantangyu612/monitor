package monitor.view.dao.mysql;

import monitor.view.domain.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/17.
 * 用户 Dao
 */
public interface UserDao {
    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM User where id = #{id}")
    User getUserById(Integer id);

    /**
     * 插入用户
     *
     * @param user
     * @return
     */
    @Insert("INSERT INTO User (id, username, name, email, phone, password, createTime) VALUES(#{id}, #{username}, #{name}, #{email}, #{phone}, #{password}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    /**
     * 获取用户列表
     *
     * @param offset
     * @param limit
     * @return
     */
    @Select("SELECT * FROM User ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    List<User> getUserList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Update("UPDATE User SET name=#{name},email=#{email},phone=#{phone} WHERE id=#{id}")
    int updateByUserId(User user);

    /**
     * 修改密码
     *
     * @param id
     * @param password
     * @return
     */
    @Update("UPDATE User SET password=#{password} WHERE id=#{id}")
    int changePassword(Integer id, String password);

    /**
     * 删除用户信息
     *
     * @param id
     * @return
     */
    @Update("DELETE FROM User WHERE id=#{id}")
    int deleteByUserId(Integer id);
}
