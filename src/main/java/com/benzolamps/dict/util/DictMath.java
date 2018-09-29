package com.benzolamps.dict.util;

/**
 * 数学工具类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-12 13:09:49
 */
public interface DictMath {

    /**
     * 将一个整数限定在另外两个数之间
     * @param value 数字
     * @param min 最小值
     * @param max 最大值
     * @return 返回值
     */
    static int limit(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}
