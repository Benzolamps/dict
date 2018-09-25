package com.benzolamps.dict.cfg;

import com.benzolamps.dict.controller.interceptor.Interceptor;
import com.benzolamps.dict.util.DictSpring;
import lombok.Setter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import java.util.List;

import static com.benzolamps.dict.util.DictLambda.tryAction;

/**
 * 用于配置SpringMVC
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-5 21:30:49
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter implements ServletContextAware, ApplicationListener<ContextRefreshedEvent> {

    /** ServletContext */
    @SuppressWarnings("unused")
    @Setter
    private ServletContext servletContext;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /* 添加拦截器 */
        List<Object> interceptors = DictSpring.getBeansOfAnnotation(Interceptor.class);
        for (Object interceptorObj : interceptors) {
            Assert.isTrue(interceptorObj instanceof HandlerInterceptor, interceptorObj.getClass().getName() + "不是HandlerInterceptor的实例");
            HandlerInterceptor interceptor = ((HandlerInterceptor) interceptorObj);
            Interceptor interceptorAnnotation = interceptor.getClass().getAnnotation(Interceptor.class);
            registry.addInterceptor(interceptor).addPathPatterns(interceptorAnnotation.pathPatterns());
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            tryAction(() -> Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:2018/dict/index.html"));
        }
    }
}
