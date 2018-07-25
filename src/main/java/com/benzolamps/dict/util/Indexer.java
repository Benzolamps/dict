package com.benzolamps.dict.util;

/**
 * 表示一个索引器, 可以进行自增和重置操作
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-12 13:14:09
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class Indexer {

    private int value;

    /** @return 相当于value++ */
    public int valuexx() {
        return value++;
    }

    /** @return 相当于++value */
    public int xxvalue() {
        return ++value;
    }

    /** @return 重置 */
    public int reset() {
        return (value = 0);
    }
}
