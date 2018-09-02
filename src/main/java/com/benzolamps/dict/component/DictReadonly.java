package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 只读
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 17:58:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DictReadonly {
}
