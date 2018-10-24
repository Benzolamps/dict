package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

import java.util.function.Predicate;

/**
 * 1个参数有返回值, 返回值类型是布尔类型的func
 * @param <T> 参数1的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:49:18
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Predicate1<T> extends Predicate<T> {
    @Override
    default boolean test(T t) {
        return this.tryExecute(t);
    }

    /**
     * 忽略异常执行
     * @param t 参数1
     * @return 返回值
     */
    @SneakyThrows
    default boolean tryExecute(T t) {
        return this.execute(t);
    }

    /**
     * 执行
     * @param t 参数1
     * @return 返回值
     * @throws Throwable 异常
     */
    boolean execute(T t) throws Throwable;
}