package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Word;

/**
 * 单词短语分组Service接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:09:20
 */
public interface WordGroupService extends GroupService {

    /**
     * 添加单词
     * @param wordGroup 单词分组
     * @param words 单词
     */
    void addWords(Group wordGroup, Word... words);
}
