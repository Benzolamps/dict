package com.benzolamps.dict.controller.interceptor;

import com.benzolamps.dict.controller.ErrorController;
import com.benzolamps.dict.controller.IndexController;
import com.benzolamps.dict.controller.UserController;
import com.benzolamps.dict.service.base.UserService;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.benzolamps.dict.controller.util.Constant.HTML;
import static com.benzolamps.dict.controller.util.Constant.JSON;

/**
 * 登录拦截器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-31 11:31:55
 */
@Interceptor
public class LoginInterceptor extends BaseInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        if (!(
            UserController.class.equals(method.getMethod().getDeclaringClass()) ||
            ErrorController.class.equals(method.getMethod().getDeclaringClass()) ||
            IndexController.class.equals(method.getMethod().getDeclaringClass()) && "ping".equals(method.getMethod().getName())
        )) {
            if (userService.getCurrent() == null) {
                if (response.getContentType().toLowerCase().contains(HTML)) {
                    response.sendRedirect(baseUrl + "/user/login.html");
                } else if (response.getContentType().toLowerCase().contains(JSON)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }
}
