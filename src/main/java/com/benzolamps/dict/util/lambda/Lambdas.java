package com.benzolamps.dict.util.lambda;

/**
 * Lambda工具类
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 13:07:08
 */
@SuppressWarnings("unused")
public interface Lambdas {

    /**
     * 忽略异常执行一个无参无返回值的action
     * @param action action
     */
    static void tryAction(Action action) {
        action.run();
    }

    /**
     * 忽略异常执行一个无参有返回值的func
     * @param func func
     * @param <T> 返回值的类型
     * @return 返回值
     */
    static <T> T tryFunc(Func<T> func) {
        return func.get();
    }
}
