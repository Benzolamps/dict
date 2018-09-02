package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.WordClazz;

/**
 * 词性Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:23:12
 */
public interface WordClazzService extends BaseService<WordClazz> {

    /**
     * 通过名称查找词性
     * @param name 名称
     * @return 词性对象
     */
    WordClazz findByName(String name);

    WordClazz findByIdOrName(String name);
}
