package com.benzolamps.dict.controller.interceptor;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作用域拦截器, 将request, response, session, application加入数据模型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-11 18:59:40
 */
@Interceptor
public class ScopeInterceptor extends BaseInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("request", request);
            modelAndView.addObject("response", response);
            modelAndView.addObject("session", request.getSession(false));
            modelAndView.addObject("application", request.getServletContext());
        }
    }
}
