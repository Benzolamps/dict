package com.benzolamps.dict.controller.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加上此注解可以支持导出Word
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 22:34:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DocView {
}
