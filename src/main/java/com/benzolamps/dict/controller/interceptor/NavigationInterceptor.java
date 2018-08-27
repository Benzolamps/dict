package com.benzolamps.dict.controller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 导航栏拦截器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-7 23:39:30
 */
@Component
public class NavigationInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        HandlerMethod method = (HandlerMethod) handler;
        if (!method.getMethod().isAnnotationPresent(NavigationView.class)) return;
        if (modelAndView != null) {
            if (modelAndView.getViewName().startsWith("redirect:")) return;
            modelAndView.addObject("content_path", modelAndView.getViewName());
            modelAndView.setViewName("view/common/nav");
        }
    }
}
