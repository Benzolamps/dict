package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

/**
 * 无参无返回值action
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:28:19
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Action extends Runnable {
    @Override
    default void run() {
        tryExecute();
    }

    /**
     * 忽略异常执行
     */
    @SneakyThrows(Throwable.class)
    default void tryExecute() {
        this.execute();
    }

    /**
     * 执行
     * @throws Throwable 异常
     */
    void execute() throws Throwable;
}
