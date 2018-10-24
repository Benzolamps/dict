package com.benzolamps.dict.util.lambda;

import lombok.experimental.UtilityClass;

/**
 * Lambda工具类
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-24 13:07:08
 */
@SuppressWarnings("unused")
@UtilityClass
public class Lambdas {

    /***
     * 忽略异常执行一个无参无返回值的action
     * @param action action
     */
    public void tryAction(Action action) {
        action.run();
    }

    /***
     * 忽略异常执行一个无参有返回值的func
     * @param func func
     * @param <T> 返回值的类型
     * @return 返回值
     */
    public <T> T tryFunc(Func<T> func) {
        return func.get();
    }
}
