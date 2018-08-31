package com.benzolamps.dict.controller.interceptor;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 拦截器注解
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-31 12:39:25
 */
@Component
@Lazy(false)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptor {

    /**
     * path patterns
     * @return path patterns
     */
    String pathPatterns() default "/**";
}
