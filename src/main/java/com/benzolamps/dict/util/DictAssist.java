package com.benzolamps.dict.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

/**
 * JavaAssist操作
 * @author Benzolamps
 * @version 2.3.9
 * @datetime 2019-1-7 17:00:47
 */
public interface DictAssist {

    /**
     * 动态获取Value值
     * @param type 类型
     * @param value 值
     * @param <T> 类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows({CannotCompileException.class, IllegalAccessException.class})
    static <T> T initialValue(Class<T> type, String value) {
        ClassPool classPool = ClassPool.getDefault();
        CtClass  ctClass = classPool.makeClass("com.benzolamps.dict.AnnotationAssist" + System.currentTimeMillis());
        CtField ctField = CtField.make("public " + type.getName() + " target;", ctClass);
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation valueAnno = new Annotation(Value.class.getName(), constPool);
        valueAnno.addMemberValue("value", new StringMemberValue(value, constPool));
        fieldAttr.addAnnotation(valueAnno);
        ctField.getFieldInfo().addAttribute(fieldAttr);
        ctClass.addField(ctField);
        Class<?> clazz = ctClass.toClass();
        DictBean<?> dictBean = new DictBean<>(clazz);
        Object bean = dictBean.createSpringBean(null);
        try {
            return (T) dictBean.getField("target").get(bean);
        } finally {
            ctClass.detach();
        }
    }
}
