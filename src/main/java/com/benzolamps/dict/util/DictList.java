package com.benzolamps.dict.util;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
        return LongStream.range(min, max).boxed().collect(Collectors.toList());
    }

    /**
     * 通过YAML字符串构建一个List
     * @param yaml YAML
     * @return list
     */
    @SuppressWarnings("rawtypes")
    static List yamlList(String yaml) {
        if (StringUtils.isEmpty(yaml)) {
            return Constant.EMPTY_LIST;
        }
        return Constant.YAML.loadAs(yaml, List.class);
    }
}
