package com.benzolamps.dict.util;

import com.benzolamps.dict.exception.DictException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.benzolamps.dict.util.DictLambda.tryAction;
import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * Bean工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 14:48:25
 */
@SuppressWarnings({"deprecation", "unchecked", "unused"})
public class DictBean<B> {

    private static final String FLASH_BEAN_NAME = "flashBean";

    /**
     * 属性
     * @param <BB> Bean类型
     * @param <P> 属性类型
     */
    public static class DictProperty<BB, P> {

        private DictBean<BB> bean;

        private PropertyDescriptor propertyDescriptor;

        /**
         * 构造器
         * @param bean bean
         * @param name 属性名
         */
        public DictProperty(DictBean<BB> bean, String name) {
            this.bean = bean;
            this.propertyDescriptor = getPropertyDescriptor(name);
        }

        /**
         * 构造器
         * @param bean bean
         * @param propertyDescriptor PropertyDescriptor对象
         */
        DictProperty(DictBean<BB> bean, PropertyDescriptor propertyDescriptor) {
            this.bean = bean;
            this.propertyDescriptor = propertyDescriptor;
        }

        /** @return 获取值 */
        public P getValue() {
            return bean.invokeMethod(propertyDescriptor.getReadMethod());
        }

        /** @param value 设置值 */
        public void setValue(Object value) {
            Assert.state(value == null || propertyDescriptor.getPropertyType().isInstance(value));
            bean.invokeMethod(propertyDescriptor.getWriteMethod(), value);
        }

        /**
         * 获取指定类型的注解
         * @param aClass 注解类型
         * @param <A> 注解类型
         * @return 注解
         */
        public <A extends Annotation> A getAnnotation(Class<A> aClass) {
            Assert.notNull(aClass);
            Field field = getField(bean.getObj().getClass(), propertyDescriptor.getName());
            Method get = propertyDescriptor.getReadMethod();
            Method set = propertyDescriptor.getWriteMethod();
            A anno = null;
            if (field.isAnnotationPresent(aClass)) {
                anno = field.getDeclaredAnnotation(aClass);
            } else if (get != null && get.isAnnotationPresent(aClass)) {
                anno = get.getDeclaredAnnotation(aClass);
            } else if (set != null && set.isAnnotationPresent(aClass)) {
                anno = set.getDeclaredAnnotation(aClass);
            }
            return anno;
        }

        /**
         * 判断是否拥有指定注解
         * @param aClass 注解类型
         * @return 判断结果
         */
        public boolean hasAnnotation(Class aClass) {
            return getAnnotation(aClass) != null;
        }

        /** @return 获取所有注解 */
        public Iterable<Annotation> getAnnotations() {
            Field field = getField(bean.getObj().getClass(), propertyDescriptor.getName());
            Method get = propertyDescriptor.getReadMethod();
            Method set = propertyDescriptor.getWriteMethod();
            Collection<Annotation> annotations = new LinkedHashSet<>();
            CollectionUtils.mergeArrayIntoCollection(field.getDeclaredAnnotations(), annotations);
            CollectionUtils.mergeArrayIntoCollection(get.getDeclaredAnnotations(), annotations);
            CollectionUtils.mergeArrayIntoCollection(set.getDeclaredAnnotations(), annotations);
            return annotations;
        }

        private PropertyDescriptor getPropertyDescriptor(String name) {
            Assert.hasLength(name);
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(bean.getObj().getClass(), name);
            if (propertyDescriptor == null) {
                throw new DictException(bean.getObj().getClass() + "." + name + " Property未找到");
            }
            return propertyDescriptor;
        }

        /** @return 获取属性类型 */
        public Class<P> getType() {
            return (Class<P>) propertyDescriptor.getPropertyType();
        }

        /** @return 获取属性名称 */
        public String getName() {
            return propertyDescriptor.getName();
        }
    }

    /* 对象 */
    private B obj;

    /* 类型 */
    private Class<? extends B> bClass;

    /**
     * 构造器
     * @param obj 对象
     */
    public DictBean(B obj) {
        Assert.notNull(obj);
        this.obj = obj;
        bClass = (Class<? extends B>) obj.getClass();
    }

    /**
     * 判断一个类是否可实例化
     * @param clazz 类
     * @return 判断结果
     */
    public static boolean classInstantiable(Class<?> clazz) {
        return !(clazz == null || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()));
    }

    /**
     * 执行一个无参方法
     * @param name 方法名
     * @param <T> 返回值类型
     * @return 返回值
     */
    public <T> T invokeMethod(String name) {
        Assert.hasLength(name);
        return invokeMethod(getMethod(name));
    }

    /**
     * 获取一个方法
     * @param name 方法名
     * @param params 参数
     * @return 方法对象
     */
    public Method getMethod(String name, Class<?>... params) {
        Assert.hasLength(name);
        return BeanUtils.findMethod(bClass, name, params);
    }

    /**
     * 执行一个方法
     * @param method 方法
     * @param args 参数
     * @param <T> 返回值类型
     * @return 返回值
     */
    public <T> T invokeMethod(Method method, Object... args) {
        Assert.notNull(method);
        return (T) tryFunc(() -> method.invoke(obj, args));
    }

    /**
     * 获取一个字段
     * @param name 字段名
     * @return 字段对象
     */
    public Field getField(String name) {
        return getField(bClass, name);
    }

    private static Field getField(Class<?> clazz, String name) {
        Assert.notNull(clazz);
        Assert.hasLength(name);
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                field = getField(superClass, name);
            }
        }
        if (field != null) {
            field.setAccessible(true);
        }

        return field;
    }

    /**
     * 获取字段值
     * @param name 字段名
     * @param <T> 字段类型
     * @return 字段值
     */
    public <T> T getFieldValue(String name) {
        Assert.notNull(obj);
        Assert.hasLength(name);
        return (T) tryFunc(() -> getField(obj.getClass(), name).get(obj));
    }

    /**
     * 设置字段值
     * @param name 字段名
     * @param value 字段值
     */
    public void setFieldValue(String name, Object value) {
        Assert.notNull(obj);
        Assert.hasLength(name);
        Field field = getField(obj.getClass(), name);
        Assert.state(value == null || field.getType().isInstance(value));
        tryAction(() -> getField(obj.getClass(), name).set(obj, value));
    }

    /** @return 获取对象 */
    public B getObj() {
        return obj;
    }

    /**
     * 获取指定属性
     * @param name 属性名
     * @param <PP> 属性类型
     * @return DictProperty对象
     */
    public <PP> DictProperty<B, PP> getProperty(String name) {
        return new DictProperty<>(this, name);
    }

    /** @return 获取所有属性 */
    public Collection<DictProperty> getProperties() {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(bClass))
            .map(propertyDescriptor -> new DictProperty(DictBean.this, propertyDescriptor))
            .collect(Collectors.toSet());
    }

    /**
     * 对一个对象中所有加了指定注解的方法进行处理
     * @param annoClazz 注解类型
     * @param action action
     * @param <A> 注解类型
     */
    public <A extends Annotation> void forEachAnnotatedMethod(Class<A> annoClazz, BiConsumer<Method, A> action) {
        Assert.notNull(annoClazz);
        Assert.notNull(action);
        for (Method method : bClass.getMethods()) {
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
        Assert.notNull(annoClazz);
        Assert.notNull(action);
        Class<?> clazz = obj.getClass();
        getProperties().stream()
            .filter(dictProperty -> dictProperty.hasAnnotation(annoClazz))
            .forEach(dictProperty -> action.accept(dictProperty, (A) dictProperty.getAnnotation(annoClazz)));
    }

    /**
     * 根据类型跟属性获取一个即时Spring bean
     * @param context 应用程序上下文
     * @param tClass 类型
     * @param properties 属性
     * @param <T> 类型
     * @return DictBean对象
     */
    @SuppressWarnings("deprecation")
    public static <T> DictBean<T> createSpringBean(ConfigurableApplicationContext context, Class<? extends T> tClass, Properties properties) {
        Assert.notNull(tClass);
        properties = properties == null ? new Properties() : properties;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(tClass);
        properties.forEach((key, value) -> beanDefinitionBuilder.addPropertyValue(DictString.toCamel(key.toString()), value));
        beanFactory.registerBeanDefinition(FLASH_BEAN_NAME, beanDefinitionBuilder.getBeanDefinition());
        T bean = (T) beanFactory.getBean(FLASH_BEAN_NAME);
        beanFactory.removeBeanDefinition(FLASH_BEAN_NAME);
        return new DictBean<>(bean);
    }

    /**
     * 获取一个普通bean
     * @param tClass 类型
     * @param properties 属性
     * @param <T> 类型
     * @return DictBean对象
     */
    public static <T> DictBean<T> createBean(Class<? extends T> tClass, Properties properties) {
        Assert.notNull(tClass);
        Assert.state(classInstantiable(tClass));
        DictBean<T> bean = new DictBean<>(BeanUtils.instantiate(tClass));
        properties = properties == null ? new Properties() : properties;
        properties.forEach((key, value) -> bean.getProperty(key.toString()).setValue(value));
        return bean;
    }
}
