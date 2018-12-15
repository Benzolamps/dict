package com.benzolamps.dict.cfg.processor;

import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictSpring;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 字段注解处理器
 * @author Benzolamps
 * @version 2.3.9
 * @datetime 2018-12-14 16:58:27
 */
@Lazy(false)
@Component
public class FieldAnnotationProcessor implements BeanPostProcessor {

    @Resource
    private List<FieldAnnotationResolver<?>> resolvers;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SneakyThrows(ReflectiveOperationException.class)
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        DictBean<?> dictBean = new DictBean<>(bean.getClass());
        for (Field field : dictBean.getFields()) {
            boolean accessible = field.isAccessible();
            if (!accessible) field.setAccessible(true);
            for (FieldAnnotationResolver resolver : resolvers) {
                ResolvableType resolvableType = ResolvableType.forClass(resolver.getClass());
                Class type = resolvableType.getInterfaces()[0].getGeneric().resolve();
                Annotation[] annotations = field.getAnnotationsByType(type);
                if (!ObjectUtils.isEmpty(annotations)) {
                    bean = DictSpring.getBean(beanName);
                    field.set(bean, resolver.resolve(annotations, bean, field));
                }
            }
            if (!accessible) field.setAccessible(false);
        }
        return bean;
    }
}
