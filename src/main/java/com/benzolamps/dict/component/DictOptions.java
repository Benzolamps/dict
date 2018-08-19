package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 用于表单的下拉框选项
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-27 22:22:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DictOptions {

    /* 可选项的数组 */
    String[] value();
}
