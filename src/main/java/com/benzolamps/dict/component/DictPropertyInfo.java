package com.benzolamps.dict.component;

import java.lang.annotation.*;

/**
 * 自定义属性注解
 * @author Benzolamps
 * @version 2.1.1
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DictPropertyInfo {
    /** @return 属性的显示名字 */
    String display() default "";

    /** @return 属性的描述 */
    String description() default "";
}
