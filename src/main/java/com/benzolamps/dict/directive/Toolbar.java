package com.benzolamps.dict.directive;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * data_list toolbar
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-22 20:23:31
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface Toolbar {

    /** html */
    String getHtml();

    /** 事件处理器 */
    String getHandler();
}
