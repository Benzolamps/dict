package com.benzolamps.dict.util.lambda;

import java.util.function.UnaryOperator;

/**
 * 1个参数有返回值, 参数类型与返回值类型一样的func
 * @param <T> 类型
 * @author Benzolamps
 * @version 2.2.3
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Operator1<T> extends UnaryOperator<T>, Func1<T, T> {
}
