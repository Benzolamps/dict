package com.benzolamps.dict.util;

import java.lang.annotation.*;

/**
 * 忽略
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 18:28:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DictIgnore {
}
