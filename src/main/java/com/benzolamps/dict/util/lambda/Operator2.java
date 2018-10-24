package com.benzolamps.dict.util.lambda;

import java.util.function.BinaryOperator;

/**
 * 2个参数有返回值, 参数类型与返回值类型一样的func
 * @param <T> 类型
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 10:47:01
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Operator2<T> extends BinaryOperator<T>, Func2<T, T, T> {
}