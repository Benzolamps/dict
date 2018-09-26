package com.benzolamps.dict.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作List的工具类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-12 15:13:23
 */
@SuppressWarnings("unused")
public interface DictList {

    /**
     * 返回一个 0 (包含) 到 length (不含) 的整数序列
     * @param length length
     * @return 序列
     */
    static List<Long> range(long length) {
        return range(0, length);
    }

    /**
     * 返回一个 min (包含) 到 max (不含) 的整数序列
     * @param min min
     * @param max max
     * @return 序列
     */
    static List<Long> range(long min, long max) {
        List<Long> list = new ArrayList<>();

        for (long i = min; i < max; i++) {
            list.add(i);
        }

        return list;
    }
}
