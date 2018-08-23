package com.benzolamps.dict.util;

import com.benzolamps.dict.exception.DictException;
import org.springframework.util.Assert;

import java.util.function.*;

/**
 * Lambda表达式工具类, 解决Lambda表达式中抛出异常的尴尬
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 13:33:05
 */
@SuppressWarnings("unused")
public interface DictLambda {

    /***
     * 忽略异常执行一个无参无返回值的action
     * @param action action
     */
    static void tryAction(Action action) {
        Assert.notNull(action, "action不能为null");
        try {
            action.execute();
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new DictException(e);
        }
    }

    /***
     * 忽略异常执行一个无参有返回值的func
     * @param func func
     * @param <R> 返回值的类型
     * @return 返回值
     */
    static <R> R tryFunc(Func<R> func) {
        Assert.notNull(func, "func不能为null");
        try {
            return func.execute();
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new DictException(e);
        }
    }

    /**
     * 无参无返回值action
     */
    @FunctionalInterface
    interface Action extends Runnable {

        @Override
        default void run() {
            tryExecute();
        }

        /**
         * 忽略异常执行
         */
        default void tryExecute() {
            tryAction(this);
        }

        /**
         * 执行
         * @throws Throwable 异常
         */
        void execute() throws Throwable;
    }

    /**
     * 1个参数无返回值action
     * @param <T> 参数1的类型
     */
    @FunctionalInterface
    interface Action1<T> extends Consumer<T> {

        @Override
        default void accept(T t) {
            tryExecute(t);
        }

        /**
         * 忽略异常执行
         * @param t 参数1
         */
        default void tryExecute(T t) {
            tryAction(() -> execute(t));
        }

        /**
         * 执行
         * @param t 参数1
         * @throws Throwable 异常
         */
        void execute(T t) throws Throwable;
    }

    /**
     * 2个参数无返回值action
     * @param <T> 参数1的类型
     * @param <U> 参数2的类型
     */
    @FunctionalInterface
    interface Action2<T, U> extends BiConsumer<T, U> {

        @Override
        default void accept(T t, U u) {
            tryExecute(t, u);
        }

        /**
         * 忽略异常执行
         * @param t 参数1
         * @param u 参数2
         */
        default void tryExecute(T t, U u) {
            tryAction(() -> execute(t, u));
        }

        /**
         * 执行
         * @param t 参数1
         * @param u 参数2
         * @throws Throwable 异常
         */
        void execute(T t, U u) throws Throwable;
    }

    /**
     * 3个参数无返回值的action
     * @param <T> 参数1的类型
     * @param <U> 参数2的类型
     * @param <V> 参数3的类型
     */
    @FunctionalInterface
    interface Action3<T, U, V> {
        /**
         * 忽略异常执行
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         */
        default void tryExecute(T t, U u, V v) {
            tryAction(() -> execute(t, u, v));
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

    /**
     * 4个参数无返回值的func
     * @param <T> 参数1的类型
     * @param <U> 参数2的类型
     * @param <V> 参数3的类型
     * @param <W> 参数4的类型
     */
    @FunctionalInterface
    interface Action4<T, U, V, W> {
        /**
         * 忽略异常执行
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         */
        default void tryExecute(T t, U u, V v, W w) {
            tryAction(() -> execute(t, u, v, w));
        }

        /**
         * 执行
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @throws Throwable 异常
         */
        void execute(T t, U u, V v, W w) throws Throwable;
    }

    /**
     * 无参有返回值func
     * @param <R> 返回值的类型
     */
    @FunctionalInterface
    interface Func<R> extends Supplier<R> {

        @Override
        default R get() {
            return tryExecute();
        }

        /**
         * 忽略异常执行
         * @return 返回值
         */
        default R tryExecute() {
            return tryFunc(this);
        }

        /**
         * 执行
         * @return 返回值
         * @throws Throwable 异常
         */
        R execute() throws Throwable;
    }

    /**
     * 1个参数有返回值的func
     * @param <T> 参数1的类型
     * @param <R> 返回值的类型
     */
    @FunctionalInterface
    interface Func1<T, R> extends Function<T, R> {

        @Override
        default R apply(T t) {
            return tryExecute(t);
        }

        /**
         * 忽略异常执行
         * @param t 参数1
         * @return 返回值
         */
        default R tryExecute(T t) {
            return tryFunc(() -> execute(t));
        }

        /**
         * 执行
         * @param t 参数1
         * @return 返回值
         * @throws Throwable 异常
         */
        R execute(T t) throws Throwable;
    }

    /**
     * 2个参数有返回值的func
     * @param <T> 参数1的类型
     * @param <U> 参数2的类型
     * @param <R> 返回值的类型
     */
    @FunctionalInterface
    interface Func2<T, U, R> extends BiFunction<T, U, R> {

        @Override
        default R apply(T t, U u) {
            return tryExecute(t, u);
        }

        /**
         * 忽略异常执行
         * @param t 参数1
         * @param u 参数2
         * @return 返回值
         */
        default R tryExecute(T t, U u) {
            return tryFunc(() -> execute(t, u));
        }

        /**
         * 执行
         * @param t 参数1
         * @param u 参数2
         * @return 返回值
         * @throws Throwable 异常
         */
        R execute(T t, U u) throws Throwable;
    }

    /**
     * 3个参数有返回值的func
     * @param <T> 参数1的类型
     * @param <U> 参数2的类型
     * @param <V> 参数3的类型
     * @param <R> 返回值的类型
     */
    @FunctionalInterface
    interface Func3<T, U, V, R> {
        /**
         * 忽略异常执行
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @return 返回值
         */
        default R tryExecute(T t, U u, V v) {
            return tryFunc(() -> execute(t, u, v));
        }

        /**
         * 执行
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @return 返回值
         * @throws Throwable 异常
         */
        R execute(T t, U u, V v) throws Throwable;
    }

    /**
     * 4个参数有返回值的func
     * @param <T> 参数1的类型
     * @param <U> 参数2的类型
     * @param <V> 参数3的类型
     * @param <W> 参数4的类型
     * @param <R> 返回值的类型
     */
    @FunctionalInterface
    interface Func4<T, U, V, W, R> {
        /**
         * 忽略异常执行
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @return 返回值
         */
        default R tryExecute(T t, U u, V v, W w) {
            return tryFunc(() -> execute(t, u, v, w));
        }

        /**
         * 执行
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @return 返回值
         * @throws Throwable 异常
         */
        R execute(T t, U u, V v, W w) throws Throwable;
    }

    /**
     * 1个参数有返回值, 参数类型与返回值类型一样的func
     * @param <T> 类型
     */
    @FunctionalInterface
    interface Operator1<T> extends UnaryOperator<T>, Func1<T, T> { }

    /**
     * 2个参数有返回值, 参数类型与返回值类型一样的func
     * @param <T> 类型
     */
    @FunctionalInterface
    interface Operator2<T> extends BinaryOperator<T>, Func2<T, T, T> { }

    /**
     * 1个参数有返回值, 返回值类型是布尔类型的func
     * @param <T> 参数1的类型
     */
    @FunctionalInterface
    interface Predicate1<T> extends Predicate<T>, Func1<T, Boolean> {
        @Override
        default boolean test(T t) {
            return tryExecute(t);
        }
    }

    /**
     * 2个参数有返回值, 返回值类型是布尔类型的func
     * @param <T> 参数1的类型
     * @param <U> 参数2的类型
     */
    @FunctionalInterface
    interface Predicate2<T, U> extends BiPredicate<T, U>, Func2<T, U, Boolean> {
        @Override
        default boolean test(T t, U u) {
            return tryExecute(t, u);
        }
    }
}

