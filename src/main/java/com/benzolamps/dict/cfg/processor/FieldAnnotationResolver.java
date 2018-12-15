package com.benzolamps.dict.cfg.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 字段注解解析器
 * @param <T> 注解类型
 * @author Benzolamps
 * @version 2.3.9
 * @datetime 2018-12-14 16:58:05
 */
public interface FieldAnnotationResolver<T extends Annotation> {

    /**
     * 解析
     * @param annotations 注解
     * @param object 对象
     * @param field 字段
     * @param <R> 字段类型
     * @return 生成的值
     * @throws ReflectiveOperationException ReflectiveOperationException
     */
    <R> R resolve(T[] annotations, Object object, Field field) throws ReflectiveOperationException;
}
