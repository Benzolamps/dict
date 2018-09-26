package com.benzolamps.dict.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

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

    private static ClassLoader classLoader;

    private static ExpressionParser expressionParser;

    /**
     * 设置applicationContext
     * @param applicationContext applicationContext
     */
    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        Assert.notNull(applicationContext, "application context不能为null");
        DictSpring.applicationContext = applicationContext;
        DictSpring.classLoader = applicationContext.getClassLoader();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(new BeanFactoryResolver(DictSpring.applicationContext.getBeanFactory()));
        expressionParser = new SpelExpressionParser() {
            @Override
            public SpelExpression doParseExpression(String expressionString, ParserContext context) {
                logger.info("spel: " + expressionString);
                SpelExpression expression = super.doParseExpression(expressionString, context);
                expression.setEvaluationContext(evaluationContext);
                return expression;
            }
        };
    }

    /**
     * 根据bean的名字获取bean
     * @param name 名字
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T getBean(String name) {
        Assert.notNull(DictSpring.applicationContext, "application context不能为null");
        Assert.hasLength(name, "name不能为null或空");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
        Assert.isTrue(containsBean(name), "未找到" + name + "对应的bean");
        if (beanFactory.isSingleton(name)) {
            return (T) beanFactory.getSingleton(name);
        } else {
            return (T) beanFactory.getBean(name);
        }
    }

    /**
     * 获取具有指定注解的bean
     * @param annotationClass 注解类型
     * @return bean列表
     */
    public static List<Object> getBeansOfAnnotation(Class<? extends Annotation> annotationClass) {
        Assert.notNull(DictSpring.applicationContext, "application context不能为null");
        Assert.notNull(annotationClass, "annotation class不能为null");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
        String[] names = beanFactory.getBeanNamesForAnnotation(annotationClass);
        return Arrays.stream(names).map(beanFactory::getBean).collect(Collectors.toList());
    }

    /**
     * 根据bean的类型获取bean
     * @param requiredType 类型
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T getBean(Class<T> requiredType) {
        Assert.notNull(DictSpring.applicationContext, "application context不能为null");
        Assert.notNull(requiredType, "required type不能为null");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
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
    public static <T> T createBean(String name, Class<?> requiredType, Properties properties, Object... args) {
        Assert.notNull(DictSpring.applicationContext, "application context不能为null");
        Assert.hasLength(name, "name不能为null或空");
        Assert.notNull(requiredType, "required type不能为null或空");
        properties = properties == null ? EMPTY_PROPERTIES : properties;
        args = args == null ? EMPTY_OBJECT_ARRAY : args;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(requiredType);
        Arrays.stream(args).forEach(beanDefinitionBuilder::addConstructorArgValue);
        properties.forEach((key, value) -> beanDefinitionBuilder.addPropertyValue(DictString.toCamel(key.toString()), value));
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
        return (T) beanFactory.getBean(name);
    }

    /**
     * 根据名称跟对象设置一个bean
     * @param name 名称
     * @param bean 对象
     */
    public static void setBean(String name, Object bean) {
        Assert.notNull(DictSpring.applicationContext, "application context不能为null");
        Assert.hasLength(name, "name不能为null或空");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
        beanFactory.registerSingleton(name, bean);
    }

    /**
     * 根据名称移除一个bean
     * @param name 名称
     */
    public static void removeBean(String name) {
        Assert.notNull(DictSpring.applicationContext, "application context不能为null");
        Assert.hasLength(name, "name不能为null或空");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
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
    public static boolean containsBean(String name) {
        Assert.notNull(DictSpring.applicationContext, "application context不能为null");
        Assert.hasLength(name, "name不能为null或空");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) DictSpring.applicationContext.getBeanFactory();
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
    
    public static <T> T spel(
            // language=SpEL
            String expression,
            Class<T> tClass) {
        if (StringUtils.isEmpty(expression)) {
            return DictObject.safeCast(expression, tClass);
        } else {
            expression = applicationContext.getEnvironment().resolvePlaceholders(expression);
            return expressionParser.parseExpression(expression).getValue(tClass);
        }
    }

    public static <T> T spel(
        // language=SpEL
        String expression) {
        return (T) spel(expression, Object.class);
    }
}
