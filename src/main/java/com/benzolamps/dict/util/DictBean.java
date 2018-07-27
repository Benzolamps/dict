package com.benzolamps.dict.util;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Bean工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 14:48:25
 */
@SuppressWarnings("unused")
@Getter
public class DictBean<B> {

    /** 临时bean的名称 */
    private static final String FLASH_BEAN_NAME = "flashBean";

    /** bean的类型 */
    private final Class<B> type;

    /** bean的方法 */
    private final Collection<Method> methods;

    /** bean的字段 */
    private final Collection<Field> fields;

    /** bean的属性 */
    private final Collection<DictProperty> properties;

    /** bean的注解 */
    private final Collection<Annotation> annotations;

    /**
     * 构造器
     * @param type 类型
     */
    public DictBean(Class<B> type) {
        Assert.notNull(type, "type不能为null");
        this.type = type;
        this.methods = internalGetMethods();
        this.fields = internalGetFields();
        this.properties = internalGetProperties();
        this.annotations = Arrays.asList(type.getAnnotations());

    }

    private Collection<Method> internalGetMethods() {
        List<Method> list = new ArrayList<>(Arrays.asList(type.getDeclaredMethods()));
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            list.addAll(0, Arrays.asList(c.getDeclaredMethods()));
        }
        AccessibleObject.setAccessible(list.toArray(new AccessibleObject[0]), true);
        return new LinkedHashSet<>(list);
    }

    private Collection<Field> internalGetFields() {
        List<Field> list = new ArrayList<>(Arrays.asList(type.getDeclaredFields()));
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            list.addAll(0, Arrays.asList(c.getDeclaredFields()));
        }
        AccessibleObject.setAccessible(list.toArray(new AccessibleObject[0]), true);
        return new LinkedHashSet<>(list);
    }

    private Collection<DictProperty> internalGetProperties() {
        List<DictProperty> dictProperties = new ArrayList<>();

        for (Field field : fields) {
            DictProperty dictProperty = new DictProperty(field.getName(), this);
            if (dictProperty.isValid()) {
                dictProperties.add(dictProperty);
            }
        }
        return new LinkedHashSet<>(dictProperties);
    }

    /**
     * 获取指定属性
     * @param name 属性名称
     * @return DictProperty
     */
    public DictProperty getProperty(String name) {
        Assert.hasLength(name, "name不能为null或空");
        return new DictProperty(name, this);
    }

    /**
     * 获取指定方法
     * @param name 方法名称
     * @param paramTypes 方法参数类型
     * @return Method
     */
    public Method getMethod(String name, Class<?>... paramTypes) {
        Assert.hasLength(name, "name不能为null或空");
        return BeanUtils.findMethod(type, name, paramTypes);
    }

    /**
     * 获取指定字段
     * @param name 字段名称
     * @return Field
     */
    public Field getField(String name) {
        Assert.hasLength(name, "name不能为null或空");
        return internalGetField(type, name);
    }

    private static Field internalGetField(Class<?> type, String name) {
        if (type == null) {
            return null;
        }
        try {
            try {
                return type.getField(name);
            } catch (NoSuchFieldException e) {
                return type.getDeclaredField(name);
            }
        } catch (NoSuchFieldException e) {
            return internalGetField(type.getSuperclass(), name);
        }
    }

    /**
     * 获取指定注解
     * @param annoClass 注解类型
     * @param <A> 注解类型
     * @return 注解对象
     */
    public <A extends Annotation> A getAnnotation(Class<A> annoClass) {
        Assert.notNull(annoClass, "anno class不能为null");
        return type.getAnnotation(annoClass);
    }

    /**
     * 对一个对象中所有加了指定注解的方法进行处理
     * @param annoClazz 注解类型
     * @param action action
     * @param <A> 注解类型
     */
    public <A extends Annotation> void forEachAnnotatedMethod(Class<A> annoClazz, BiConsumer<Method, A> action) {
        Assert.notNull(annoClazz, "anno class不能为null");
        Assert.notNull(action, "action不能为null");
        for (Method method : methods) {
            if (method.isAnnotationPresent(annoClazz)) {
                A anno = method.getDeclaredAnnotation(annoClazz);
                action.accept(method, anno);
            }
        }
    }

    /**
     * 对一个对象中所有加了指定注解的方法进行处理
     * @param annoClazz 注解类型
     * @param action action
     * @param <A> 注解类型
     */
    public <A extends Annotation> void forEachAnnotatedProperty(Class<A> annoClazz, BiConsumer<DictProperty, A> action) {
        Assert.notNull(annoClazz, "anno class不能为null");
        Assert.notNull(action, "action不能为null");
        getProperties().stream()
            .filter(dictProperty -> dictProperty.hasAnnotation(annoClazz))
            .forEach(dictProperty -> action.accept(dictProperty, dictProperty.getAnnotation(annoClazz)));
    }

    /**
     * 根据类型跟属性获取一个即时Spring bean
     * @param context 应用程序上下文
     * @param properties 属性
     * @return DictBean对象
     */
    @SuppressWarnings({"deprecation", "unchecked"})
    public B createSpringBean(ConfigurableApplicationContext context, Properties properties) {
        Assert.notNull(context, "context不能为null");
        properties = properties == null ? new Properties() : properties;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(type);
        properties.forEach((key, value) -> beanDefinitionBuilder.addPropertyValue(DictString.toCamel(key.toString()), value));
        beanFactory.registerBeanDefinition(FLASH_BEAN_NAME, beanDefinitionBuilder.getBeanDefinition());
        B obj = (B) beanFactory.getBean(FLASH_BEAN_NAME);
        beanFactory.removeBeanDefinition(FLASH_BEAN_NAME);
        return obj;
    }

    /**
     * 获取一个普通bean
     * @param properties 属性
     * @return DictBean对象
     */
    public B createBean(Properties properties) {
        Assert.isTrue(instantiable(), "类必须是可实例化的");
        final B obj = BeanUtils.instantiate(type);
        properties = properties == null ? new Properties() : properties;
        properties.forEach((key, value) -> getProperty(key.toString()).set(obj, value));
        return obj;
    }

    /**
     * 是否可实例化
     * @return 判断结果
     */
    public boolean instantiable() {
        return !(type == null || type.isInterface() || Modifier.isAbstract(type.getModifiers()));
    }
}
