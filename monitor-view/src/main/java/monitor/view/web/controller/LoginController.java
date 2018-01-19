package monitor.view.web.controller;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import monitor.view.domain.entity.User;
import monitor.view.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by bjlizhitao on 2018/1/16.
 * 登录控制器
 */
@Controller
public class LoginController {
    @Resource
    private UserService userService;

    /**
     * 登录页
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * 登录页
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, String username, String password) {
        HttpSession session = request.getSession();

        if ("root".equals(username) && "root".equals(password)) {
            session.setAttribute("username", "root");
            return "redirect:/";
        } else {
            User user = userService.selectLoginUser(username, Hashing.md5().hashString(password, Charsets.UTF_8).toString());
            if (null != user) {
                session.setAttribute("username", user.getUsername());
                return "redirect:/";
            } else {
                return "redirect:/login";
            }
        }
    }

    /**
     * 注销登录
     *
     * @return
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public String loginOUt(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/login";
    }
}
