package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

/**
 * 3个参数无返回值的action
 * @param <T> 参数1的类型
 * @param <U> 参数2的类型
 * @param <V> 参数3的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:39:30
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Action3<T, U, V> {
    /**
     * 忽略异常执行
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     */
    @SneakyThrows
    default void tryExecute(T t, U u, V v) {
        this.execute(t, u, v);
    }

    /**
     * 执行
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @throws Throwable 异常
     */
    void execute(T t, U u, V v) throws Throwable;
}
