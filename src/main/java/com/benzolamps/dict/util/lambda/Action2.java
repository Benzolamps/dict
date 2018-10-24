package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

import java.util.function.BiConsumer;

/**
 * 2个参数无返回值action
 * @param <T> 参数1的类型
 * @param <U> 参数2的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:37:55
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Action2<T, U> extends BiConsumer<T, U> {
    @Override
    default void accept(T t, U u) {
        this.tryExecute(t, u);
    }

    /**
     * 忽略异常执行
     * @param t 参数1
     * @param u 参数2
     */
    @SneakyThrows
    default void tryExecute(T t, U u) {
        this.execute(t, u);
    }

    /**
     * 执行
     * @param t 参数1
     * @param u 参数2
     * @throws Throwable 异常
     */
    void execute(T t, U u) throws Throwable;
}