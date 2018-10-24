package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

/**
 * 3个参数有返回值的func
 * @param <T> 参数1的类型
 * @param <U> 参数2的类型
 * @param <V> 参数3的类型
 * @param <R> 返回值的类型
 * @author Benzolamps
 * @datetime 2018-10-24 10:44:24
 */
@SuppressWarnings("unused")
@FunctionalInterface
interface Func3<T, U, V, R> {
    /**
     * 忽略异常执行
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @return 返回值
     */
    @SneakyThrows
    default R tryExecute(T t, U u, V v) {
        return execute(t, u, v);
    }

    /**
     * 执行
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @return 返回值
     * @throws Throwable 异常
     */
    R execute(T t, U u, V v) throws Throwable;
}