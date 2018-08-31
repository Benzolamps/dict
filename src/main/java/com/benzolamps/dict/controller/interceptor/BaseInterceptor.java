package com.benzolamps.dict.controller.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-31 11:51:03
 */
public abstract class BaseInterceptor extends HandlerInterceptorAdapter {

    @Value("#{servletContext.contextPath}")
    protected String baseUrl;
}
