package com.benzolamps.dict.util;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;

/**
 * 常量
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-23 19:10:34
 */
@SuppressWarnings("unchecked")
public interface Constant {

    String DATE_FORMAT = "yyyy-MM-dd";

    String TIME_FORMAT = "HH:mm:ss";

    DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT);

    // language=RegExp
    String IDENTIFIER_PATTERN = "^[$_a-zA-Z][$_a-zA-Z0-9]*$";

    /** 空的Object数组 */
    Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /** 空的String数组 */
    String[] EMPTY_STRING_ARRAY = new String[0];

    /** 空的Method数组 */
    Method[] EMPTY_METHOD_ARRAY = new Method[0];

    /** 空的List */
    List EMPTY_LIST = Collections.EMPTY_LIST;

    /** 空的Set */
    Set EMPTY_SET = Collections.EMPTY_SET;

    /** 空的Collection */
    Collection EMPTY_COLLECTION = Collections.EMPTY_LIST;

    /** 空的Map */
    Map EMPTY_MAP = Collections.EMPTY_MAP;

    /** 空的Properties */
    Properties EMPTY_PROPERTIES = ((Supplier<Properties>) () -> {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Properties.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, methodProxy) ->
            method.getDeclaringClass().isInstance(EMPTY_MAP) ? methodProxy.invoke(EMPTY_MAP, args) : methodProxy.invokeSuper(obj, args));
        return (Properties) enhancer.create();
    }).get();
}