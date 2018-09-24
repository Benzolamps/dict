package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 实体类别名
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-09-23 21:59:24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Alias {

    /** 别名 */
    String value();
}
