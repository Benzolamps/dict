package com.benzolamps.dict.controller.interceptor;

import java.lang.annotation.*;

/**
 * 加上此注解视图将加上导航栏
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-6 01:11:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NavigationView {
}
