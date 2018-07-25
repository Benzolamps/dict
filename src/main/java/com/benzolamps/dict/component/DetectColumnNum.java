package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 配合excel导入使用, 用于指定检测表格的列数
 * @author Benzolamps
 * @version 1.1.5
 * @datetime 2018-6-26 15:38:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface DetectColumnNum {
    /** @return 表格的列数 */
    int value();
}
