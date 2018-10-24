package com.benzolamps.dict.util;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Property工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-26 23:43:06
 */
@Getter
public class DictProperty {

    /** 属性名称 */
    private final String name;

    /** DictBean对象 */
    private final DictBean<?> bean;

    /** 属性类型 */
    private final Class<?> type;

    /** 属性字段 */
    private final Field field;

    /** 属性对应的get方法 */
    private final Method get;

    /** 属性对应的set方法 */
    private final Method set;

    /** 属性的注解 */
    private final Collection<Annotation> annotations;

    /**
     * 构造器
     * @param name 属性名称
     * @param bean DictBean对象
     */
    public DictProperty(String name, DictBean<?> bean) {
        Assert.notNull(name, "name不能为null");
        Assert.notNull(bean, "bean不能为null");
        this.name = name;
        this.bean = bean;
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(bean.getType(), name);
        this.get = propertyDescriptor == null ? null : propertyDescriptor.getReadMethod();
        this.set = propertyDescriptor == null ? null : propertyDescriptor.getWriteMethod();
        this.field = bean.getField(name);
        this.type = field == null ? null : field.getType();
        this.annotations = internalGetAnnotations();
    }

    /**
     * 获取指定类型的注解
     * @param annoClass 注解类型
     * @param <A> 注解类型
     * @return 注解对象
     */
    public <A extends Annotation> A getAnnotation(Class<A> annoClass) {
        Assert.notNull(annoClass, "anno class不能为null");
        if (field != null && field.isAnnotationPresent(annoClass)) {
            return field.getAnnotation(annoClass);
        } else if (get != null && get.isAnnotationPresent(annoClass)) {
            return get.getAnnotation(annoClass);
        } if (set != null && set.isAnnotationPresent(annoClass)) {
            return set.getAnnotation(annoClass);
        }
        return null;
    }

    /**
     * 判断是否有指定类型的注解
     * @param annoClass 注解类型
     * @return 判断结果
     */
    @SuppressWarnings("RedundantIfStatement")
    public boolean hasAnnotation(Class<? extends Annotation> annoClass) {
        Assert.notNull(annoClass, "anno class不能为null");
        if (field != null && field.isAnnotationPresent(annoClass)) {
            return true;
        } else if (get != null && get.isAnnotationPresent(annoClass)) {
            return true;
        } if (set != null && set.isAnnotationPresent(annoClass)) {
            return true;
        } else {
            return false;
        }
    }

    private Collection<Annotation> internalGetAnnotations() {
        List<Annotation> annotations = new ArrayList<>();
        if (field != null) {
            annotations.addAll(Arrays.asList(field.getDeclaredAnnotations()));
        }
        if (get != null) {
            annotations.addAll(Arrays.asList(get.getDeclaredAnnotations()));
        }
        if (set != null) {
            annotations.addAll(Arrays.asList(set.getDeclaredAnnotations()));
        }
        return new LinkedHashSet<>(annotations);
    }

    /**
     * 判断属性是否可用
     * @return 判断结果
     */
    public boolean isValid() {
        return field != null
            && !Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())
            && get != null && set != null;
    }

    /**
     * 对一个对象获取值
     * @param obj 对象
     * @param <T> 类型
     * @return 值
     */
    @SneakyThrows(ReflectiveOperationException.class)
    @SuppressWarnings("unchecked")
    public <T> T get(Object obj) {
        Assert.notNull(obj, "obj不能为null");
        Assert.isTrue(bean.getType().isInstance(obj), "obj必须是" + bean.getType() + "的实例");
        return get == null ? null : (T) get.invoke(obj);
    }

    /**
     * 对一个对象设置值
     * @param obj 对象
     * @param value 值
     */
    @SneakyThrows(ReflectiveOperationException.class)
    public void set(Object obj, Object value) {
        Assert.notNull(obj, "obj不能为null");
        Assert.isTrue(bean.getType().isInstance(obj), "obj必须是" + bean.getType() + "的实例");
        if (set != null) set.invoke(obj, value);
    }
}
