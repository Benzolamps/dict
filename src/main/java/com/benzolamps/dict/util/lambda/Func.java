package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

import java.util.function.Supplier;

/**
 * 无参有返回值func
 * @param <R> 返回值的类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:41:23
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Func<R> extends Supplier<R> {

    @Override
    default R get() {
        return this.tryExecute();
    }

    /**
     * 忽略异常执行
     * @return 返回值
     */
    @SneakyThrows
    default R tryExecute() {
        return this.execute();
    }

    /**
     * 执行
     * @return 返回值
     * @throws Throwable 异常
     */
    R execute() throws Throwable;
}
