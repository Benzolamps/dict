package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

import java.util.function.Function;

/**
 * 1个参数有返回值的func
 * @param <T> 参数1的类型
 * @param <R> 返回值的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:42:52
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Func1<T, R> extends Function<T, R> {

    @Override
    default R apply(T t) {
        return tryExecute(t);
    }

    /**
     * 忽略异常执行
     * @param t 参数1
     * @return 返回值
     */
    @SneakyThrows
    default R tryExecute(T t) {
        return execute(t);
    }

    /**
     * 执行
     * @param t 参数1
     * @return 返回值
     * @throws Throwable 异常
     */
    R execute(T t) throws Throwable;
}