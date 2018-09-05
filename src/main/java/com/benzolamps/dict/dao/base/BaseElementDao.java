package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.Library;

import java.util.Set;

/**
 * 单词或短语类的基类Dao接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-5 22:36:38
 */
public interface BaseElementDao<T extends BaseElement> extends BaseDao<T> {

    /**
     * 获取不存在的单词或短语原形
     * @param prototypes 原形
     * @param library 词库
     * @return 不存在的单词或短语原形
     */
    Set<String> findNotContainsPrototype(Set<String> prototypes, Library library);
}
