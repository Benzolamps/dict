package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 远程验证
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-19 12:46:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DictRemote {

    /** @return url */
    String value();
}
