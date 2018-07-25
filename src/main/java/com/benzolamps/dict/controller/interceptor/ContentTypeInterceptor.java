package com.benzolamps.dict.controller.interceptor;

import com.benzolamps.dict.util.DictWeb;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ContentType拦截器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-11 18:53:34
 */
@Component
public class ContentTypeInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String url = request.getRequestURL().toString();
        url = url.split("\\?")[0];
        int index = url.lastIndexOf(".") + 1;
        Assert.state(index >= 0, "没有扩展名");
        url = url.substring(index);
        String contentType = DictWeb.convertContentType(url);
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
