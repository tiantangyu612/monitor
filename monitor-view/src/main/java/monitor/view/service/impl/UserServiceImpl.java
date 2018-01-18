package monitor.view.service.impl;

import monitor.view.dao.mysql.UserDao;
import monitor.view.domain.entity.User;
import monitor.view.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/18.
 * 用户管理 Service
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    /**
     * 修改用户信息
     *
     * @param user
     */
    @Override
    public int update(User user) {
        return userDao.updateByUserId(user);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        return userDao.deleteByUserId(id);
    }

    /**
     * 查询用户总数
     *
     * @return
     */
    @Override
    public int count() {
        return userDao.count();
    }

    /**
     * 插入用户信息
     *
     * @param user
     * @return
     */
    @Override
    public User insert(User user) {
        userDao.insert(user);
        return user;
    }

    /**
     * 根据 id 获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    /**
     * 获取用户列表
     *
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<User> getUserList(Integer offset, Integer limit) {
        return userDao.getUserList(offset, limit);
    }

    /**
     * 查询登录用户
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User selectLogingUser(String username, String password) {
        return userDao.selectLoginUser(username, password);
    }
}
