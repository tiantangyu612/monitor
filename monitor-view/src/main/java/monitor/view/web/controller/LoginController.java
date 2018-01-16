package monitor.view.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by bjlizhitao on 2018/1/16.
 * 登录控制器
 */
@Controller
public class LoginController {
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
        if ("root".equals(username) && "root".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", "root");
            return "redirect:/";
        } else {
            return "redirect:/login";
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
