package com.benzolamps.dict.bean;

import java.lang.annotation.*;

/**
 * 实体类格式注解
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 00:13:01
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Format {

    /** @return 实体类中的方法名 */
    String value();
}
