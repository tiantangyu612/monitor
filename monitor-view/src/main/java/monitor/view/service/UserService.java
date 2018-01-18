package monitor.view.service;

import monitor.view.domain.entity.User;

import java.util.List;

/**
 * Created by bjlizhitao on 2018/1/18.
 * 用户管理 Service
 */
public interface UserService {
    /**
     * 修改用户信息
     *
     * @param user
     */
    int update(User user);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 查询用户总数
     *
     * @return
     */
    int count();

    /**
     * 插入用户信息
     *
     * @param user
     * @return
     */
    User insert(User user);

    /**
     * 根据 id 获取用户信息
     *
     * @param id
     * @return
     */
    User getUserById(Integer id);

    /**
     * 获取用户列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<User> getUserList(Integer offset, Integer limit);

    /**
     * 查询登录用户
     *
     * @param username
     * @param password
     * @return
     */
    User selectLogingUser(String username, String password);
}
