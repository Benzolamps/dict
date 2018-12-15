package com.benzolamps.dict.cfg.processor.annotation;

import com.benzolamps.dict.cfg.processor.FieldAnnotationResolver;
import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictObject;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.lang.annotation.*;
import java.lang.reflect.Field;

import static com.benzolamps.dict.util.Constant.UTF8_CHARSET;

/**
 * 将Resource中读取内容
 * @author Benzolamps
 * @version 2.3.9
 * @datetime 2018-12-15 10:55:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface ResourceContent {

    /** Resource路径 */
    String value();
}

@Lazy(false)
@Component
class ResourceContentResolver implements FieldAnnotationResolver<ResourceContent> {
    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows(Exception.class)
    public <R> R resolve(ResourceContent[] annotations, Object object, Field field) {
        ResourceContent resourceContent = annotations[0];
        Assert.hasText(resourceContent.value(), "value不能为null或空");
        org.springframework.core.io.Resource resource;
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("com.benzolamps.dict.JavaAssistTemp");
        CtField ctField = CtField.make("public org.springframework.core.io.Resource resource;", ctClass);
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        javassist.bytecode.annotation.Annotation value = new Annotation("org.springframework.beans.factory.annotation.Value", constPool);
        value.addMemberValue("value", new StringMemberValue(resourceContent.value(), constPool));
        fieldAttr.addAnnotation(value);
        ctField.getFieldInfo().addAttribute(fieldAttr);
        ctClass.addField(ctField);
        Class<?> clazz = ctClass.toClass();
        DictBean<?> dictBean = new DictBean<>(clazz);
        Object bean = dictBean.createSpringBean(null);
        resource = (Resource) dictBean.getField("resource").get(bean);
        if (field.getType().equals(byte[].class)) return (R) StreamUtils.copyToByteArray(resource.getInputStream());
        else return (R) DictObject.ofObject(StreamUtils.copyToString(resource.getInputStream(), UTF8_CHARSET), field.getType());
    }
}

