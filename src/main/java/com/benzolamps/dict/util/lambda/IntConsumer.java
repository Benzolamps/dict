/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package com.benzolamps.dict.util.lambda;

import lombok.SneakyThrows;

/**
 * IntConsumer
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:55:36
 */
@FunctionalInterface
public interface IntConsumer extends java.util.function.IntConsumer {
    @Override
    default void accept(int i) {
        this.tryExecute(i);
    }

    /**
     * 忽略异常执行
     * @param i 参数
     */
    @SneakyThrows(Throwable.class)
    default void tryExecute(int i) {
        this.execute(i);
    }

    /**
     * 执行
     * @param i 参数
     * @throws Throwable 异常
     */
    void execute(int i) throws Throwable;
}
