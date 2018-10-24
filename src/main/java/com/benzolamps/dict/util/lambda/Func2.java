package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

import java.util.function.BiFunction;

/**
 * 2个参数有返回值的func
 * @param <T> 参数1的类型
 * @param <U> 参数2的类型
 * @param <R> 返回值的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:43:42
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Func2<T, U, R> extends BiFunction<T, U, R> {

    @Override
    default R apply(T t, U u) {
        return tryExecute(t, u);
    }

    /**
     * 忽略异常执行
     * @param t 参数1
     * @param u 参数2
     * @return 返回值
     */
    @SneakyThrows
    default R tryExecute(T t, U u) {
        return execute(t, u);
    }

    /**
     * 执行
     * @param t 参数1
     * @param u 参数2
     * @return 返回值
     * @throws Throwable 异常
     */
    R execute(T t, U u) throws Throwable;
}