package monitor.view.web.controller.manage;

import monitor.view.domain.entity.User;
import monitor.view.domain.vo.Pager;
import monitor.view.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/16.
 * 用户管理
 */
@Controller
@RequestMapping("/monitor/manage")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户管理界面
     *
     * @return
     */
    @RequestMapping("/users")
    public String manage(Model model, @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage) {
        int count = userService.count();

        Pager<User> pager = new Pager<User>(10, count, currentPage);
        List<User> products = userService.getUserList(pager.getOffset(), pager.getPageSize());

        pager.setDataList(products);

        model.addAttribute("pager", pager);

        return "manage/user/user";
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String add(User user) {
        userService.insert(user);

        return "redirect:/monitor/manage/users";
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/delete/{id}")
    public String delete(@PathVariable(value = "id") Integer id) {
        userService.deleteById(id);

        return "redirect:/monitor/manage/users";
    }

    /**
     * 更新用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/update/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public User update(@PathVariable(value = "id") Integer id) {
        return userService.getUserById(id);
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public String update(User user) {
        userService.update(user);

        return "redirect:/monitor/manage/users";
    }

    /**
     * 重置用户密码
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/resetPassword/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public boolean resetPassword(@PathVariable(value = "id") Integer id) {
        return userService.resetUserPassword(id) > 0;
    }
}
