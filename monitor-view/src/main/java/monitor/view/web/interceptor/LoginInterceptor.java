package monitor.view.web.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by bjlizhitao on 2018/1/16.
 * 登录拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();

        if (!uri.startsWith("/login")) {
            HttpSession httpSession = request.getSession();
            Object usernameObj = httpSession.getAttribute("username");
            if (null == usernameObj || StringUtils.isBlank((String) usernameObj)) {
                response.sendRedirect("/login");
                return false;
            }
        }

        return true;
    }
}
