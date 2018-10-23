package com.benzolamps.dict.util;

import com.benzolamps.dict.exception.DictException;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

/**
 * 枚举工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-12 16:33:52
 */
public interface DictEnum {

    /**
     * 根据枚举类型获取枚举的各个值
     * @param enumClass 枚举类型
     * @return 枚举列表
     */
    @SneakyThrows(ReflectiveOperationException.class)
    static List<? extends Enum<?>> getEnumList(Class<? extends Enum<?>> enumClass) {
        Enum<?>[] enums = (Enum<?>[]) enumClass.getDeclaredMethod("values").invoke(null);
        return Arrays.asList(enums);
    }
}
