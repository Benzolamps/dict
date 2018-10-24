package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

import java.util.function.Consumer;

/**
 * 1个参数无返回值action
 * @param <T> 参数1的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:35:46
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Action1<T> extends Consumer<T> {
    @Override
    default void accept(T t) {
        this.tryExecute(t);
    }

    /**
     * 忽略异常执行
     * @param t 参数1
     */
    @SneakyThrows(Throwable.class)
    default void tryExecute(T t) {
        this.execute(t);
    }

    /**
     * 执行
     * @param t 参数1
     * @throws Throwable 异常
     */
    void execute(T t) throws Throwable;
}
