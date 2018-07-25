package com.benzolamps.dict.cfg;

import com.benzolamps.dict.controller.interceptor.ContentTypeInterceptor;
import com.benzolamps.dict.controller.interceptor.NavigationInterceptor;
import com.benzolamps.dict.controller.interceptor.ScopeInterceptor;
import com.benzolamps.dict.controller.interceptor.WindowInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 用于配置SpringMVC
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-5 21:30:49
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /* 添加拦截器 */
        registry.addInterceptor(new NavigationInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ContentTypeInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ScopeInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new WindowInterceptor()).addPathPatterns("/**");
    }
}
