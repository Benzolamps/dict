package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 实体类集合大小注解
 *
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 20:59:06
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Size {

    /** 属性名 */
    String value();
}
