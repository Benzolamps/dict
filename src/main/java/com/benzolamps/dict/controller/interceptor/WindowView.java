package com.benzolamps.dict.controller.interceptor;

import java.lang.annotation.*;

/**
 * 加上此注解可以支持弹窗
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 22:31:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WindowView {
}
