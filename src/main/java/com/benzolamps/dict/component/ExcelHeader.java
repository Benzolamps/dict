package com.benzolamps.dict.component;

import java.lang.annotation.*;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;

/**
 * 用于表格导入时将表格内容与实体类属性一一对应
 * @author Benzolamps
 * @version 1.1.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(ElementType.FIELD)
public @interface ExcelHeader {

    /** 表示数字值的取值范围 */
    @interface Range {

        /** @return 最小值 */
        double min() default MIN_VALUE;

        /** @return 最大值 */
        double max() default MAX_VALUE;
    }

    /** @return 所在列 */
    int value();

    /** @return 列的格式 */
    Class<?> cellClass() default String.class;

    /** @return not null */
    boolean notEmpty() default false;

    /** @return 取值范围 */
    Range range() default @Range;
}


