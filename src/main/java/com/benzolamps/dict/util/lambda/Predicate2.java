package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

import java.util.function.BiPredicate;

/**
 * 2个参数有返回值, 返回值类型是布尔类型的func
 * @param <T> 参数1的类型
 * @param <U> 参数2的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:49:59
 */
@SuppressWarnings("unused")
@FunctionalInterface
interface Predicate2<T, U> extends BiPredicate<T, U> {
    @Override
    default boolean test(T t, U u) {
        return tryExecute(t, u);
    }

    /**
     * 忽略异常执行
     * @param t 参数1
     * @param u 参数2
     * @return 返回值
     */
    @SneakyThrows
    default boolean tryExecute(T t, U u) {
        return this.execute(t, u);
    }

    /**
     * 执行
     * @param t 参数1
     * @param u 参数2
     * @return 返回值
     * @throws Throwable 异常
     */
    boolean execute(T t, U u) throws Throwable;
}