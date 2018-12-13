package com.benzolamps.dict.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.benzolamps.dict.util.Constant.EMPTY_OBJECT_ARRAY;
import static com.benzolamps.dict.util.Constant.EMPTY_PROPERTIES;

/**
 * Spring工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-23 19:34:21
 */
@Slf4j
@SuppressWarnings({"unchecked", "unused"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DictSpring {

    private static ConfigurableApplicationContext applicationContext;

    private static DefaultListableBeanFactory beanFactory;

    private static BeanExpressionContext beanExpressionContext;

    private static ClassLoader classLoader;

    private static void assertNull() {
        Assert.notNull(applicationContext, "application context不能为null");
    }

    /**
     * 设置applicationContext
     * @param applicationContext applicationContext
     */
    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        DictSpring.applicationContext = applicationContext;
        DictSpring.classLoader = applicationContext.getClassLoader();
        DictSpring.beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        DictSpring.beanExpressionContext = new BeanExpressionContext(beanFactory, null);
    }

    /**
     * 根据bean的名字获取bean
     * @param name 名字
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T getBean(@Language("spring-bean-name") String name) {
        assertNull();
        Assert.hasText(name, "name不能为null或空");
        Assert.isTrue(DictSpring.containsBean(name), "未找到" + name + "对应的bean");
        if (beanFactory.isSingleton(name)) {
            return (T) beanFactory.getSingleton(name);
        } else {
            return (T) beanFactory.getBean(name);
        }
    }

    /**
     * 根据bean的名字获取bean
     * @param name 名字
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T getBean(@Language("spring-bean-name") String name, Class<T> requiredType) {
        assertNull();
        Assert.hasText(name, "name不能为null或空");
        Assert.isTrue(DictSpring.containsBean(name), "未找到" + name + "对应的bean");
        if (beanFactory.isSingleton(name)) {
            return (T) beanFactory.getSingleton(name);
        } else {
            return beanFactory.getBean(name, requiredType);
        }
    }

    /**
     * 获取具有指定注解的bean
     * @param annotationClass 注解类型
     * @return bean列表
     */
    public static List<Object> getBeansOfAnnotation(Class<? extends Annotation> annotationClass) {
        assertNull();
        Assert.notNull(annotationClass, "annotation class不能为null");
        String[] names = beanFactory.getBeanNamesForAnnotation(annotationClass);
        return Stream.of(names).map(beanFactory::getBean).collect(Collectors.toList());
    }

    /**
     * 根据bean的类型获取bean
     * @param requiredType 类型
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertNull();
        Assert.notNull(requiredType, "required type不能为null");
        Map<String, T> beans = beanFactory.getBeansOfType(requiredType);
        Assert.isTrue(beans != null && beans.size() > 0, "未找到" + requiredType.getName() + "对应的bean");
        Assert.isTrue(beans.size() == 1, requiredType.getName() + "对应的bean不唯一");
        return beans.values().iterator().next();
    }

    /**
     * 根据名称、类型、属性和参数创建一个bean
     * @param name 名称
     * @param requiredType 类型
     * @param properties 属性
     * @param args 参数
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T createBean(@Language("spring-bean-name") String name, Class<?> requiredType, Properties properties, Object... args) {
        assertNull();
        Assert.hasText(name, "name不能为null或空");
        Assert.notNull(requiredType, "required type不能为null");
        properties = properties == null ? EMPTY_PROPERTIES : properties;
        args = args == null ? EMPTY_OBJECT_ARRAY : args;
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(requiredType);
        Stream.of(args).forEach(beanDefinitionBuilder::addConstructorArgValue);
        properties.forEach((key, value) -> beanDefinitionBuilder.addPropertyValue(DictString.toCamel(key.toString()), value));
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
        return (T) beanFactory.getBean(name);
    }

    /**
     * 根据名称跟对象设置一个bean
     * @param name 名称
     * @param bean 对象
     */
    public static void setBean(@Language("spring-bean-name") String name, Object bean) {
        assertNull();
        Assert.hasText(name, "name不能为null或空");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
        beanFactory.registerSingleton(name, bean);
    }

    /**
     * 根据名称移除一个bean
     * @param name 名称
     */
    public static void removeBean(@Language("spring-bean-name") String name) {
        assertNull();
        Assert.hasText(name, "name不能为null或空");
        Assert.isTrue(containsBean(name), "未找到" + name + "对应的bean");
        if (beanFactory.isSingleton(name)) {
            beanFactory.destroySingleton(name);
        } else {
            beanFactory.removeBeanDefinition(name);
        }
    }

    /**
     * 根据名称判断一个bean是否存在
     * @param name 名称
     * @return 判断结果
     */
    public static boolean containsBean(@Language("spring-bean-name") String name) {
        assertNull();
        Assert.hasText(name, "name不能为null或空");
        return beanFactory.containsSingleton(name) || beanFactory.containsBeanDefinition(name);
    }

    /**
     * 获取ClassLoader
     * @return ClassLoader
     */
    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * 设置ClassLoader
     * @param classLoader ClassLoader
     */
    public static void setClassLoader(ClassLoader classLoader) {
        DictSpring.classLoader = classLoader;
    }

    /**
     * 执行spel
     * @param expression 表达式
     * @param tClass 类型
     * @param <T> 类型
     * @return 结果
     */
    public static <T> T resolve(@Language("SpEL") String expression, Class<T> tClass) {
        assertNull();
        String placeholdersResolved = beanFactory.resolveEmbeddedValue(expression);
        BeanExpressionResolver beanExpressionResolver = beanFactory.getBeanExpressionResolver();
        if (beanExpressionResolver == null) {
            return (T) expression;
        }
        return (T) beanExpressionResolver.evaluate(placeholdersResolved, beanExpressionContext);
    }

    /**
     * 执行spel
     * @param expression 表达式
     * @param tClass 类型
     * @param <T> 类型
     * @return 结果
     */
    public static <T> T $(@Language("SpEL") String expression, Class<T> tClass) {
        return resolve(expression, tClass);
    }

    /**
     * 执行spel
     * @param expression 表达式
     * @param <T> 类型
     * @return 结果
     */
    public static <T> T $(@Language("SpEL") String expression) {
        return (T) $(expression, Object.class);
    }
}
