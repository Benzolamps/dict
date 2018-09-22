package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 表别名
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-22 21:41:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface Alias {

    /** 别名 */
    String value();
}
