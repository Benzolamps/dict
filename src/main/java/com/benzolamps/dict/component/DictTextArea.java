package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 是否使用TextArea
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 17:59:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DictTextArea {
}
