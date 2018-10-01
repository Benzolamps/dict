package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;

/**
 * 单词短语分组Service接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:50:01
 */
public interface PhraseGroupService extends GroupService {

    /**
     * 添加短语
     * @param phraseGroup 短语分组
     * @param phrases 短语
     */
    void addPhrases(Group phraseGroup, Phrase... phrases);

    /**
     *
     * @param phraseGroup 单词分组
     * @param phrases 单词
     */
    void removePhrases(Group phraseGroup, Phrase... phrases);

    /**
     * 评分
     * @param phraseGroup 单词分组
     * @param student 学生
     * @param phrases 已掌握的单词
     */
    void scorePhrases(Group phraseGroup, Student student, Phrase... phrases);
}
