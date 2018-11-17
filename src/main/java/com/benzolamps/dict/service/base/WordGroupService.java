package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.vo.ProcessImportVo;

import java.util.Collection;
import java.util.List;

/**
 * 单词分组Service接口
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

    /**
     *
     * @param wordGroup 单词分组
     * @param words 单词
     */
    void removeWords(Group wordGroup, Word... words);

    /**
     * 评分
     * @param wordGroup 单词分组
     * @param student 学生
     * @param words 已掌握的单词
     */
    void scoreWords(Group wordGroup, Student student, Word... words);

    /**
     * 导入学习进度
     * @param processImportVos ProcessImportVo
     */
    void importWords(ProcessImportVo... processImportVos);

    /**
     * 创建派生分组
     * @param original 原分组
     * @param words 单词集合
     * @param wordGroup 分组
     * @return 派生分组
     */
    Group extractDeriveGroup(Group original, Collection<Word> words, Collection<Student> students, Group wordGroup);

    /**
     * 创建派生分组
     * @param original 原分组
     * @param students 学生集合
     * @param wordGroup 分组
     * @return 专属分组集合
     */
    Collection<Group> extractPersonalGroup(Group original, Collection<Student> students, Group wordGroup);

    /**
     * 添加词频分组 (TXT)
     * @param wordGroup 分组
     * @param bytes byte[]
     * @param extraWords 词库中不存在的单词
     */
    Group persistFrequencyGroupTxt(Group wordGroup, byte[] bytes, List<String> extraWords);

    /**
     * 添加词频分组 (DOC)
     * @param wordGroup 分组
     * @param bytes byte[]
     * @param extraWords 词库中不存在的单词
     */
    Group persistFrequencyGroupDoc(Group wordGroup, byte[] bytes, List<String> extraWords);

    /**
     * 更新词频分组 (TXT)
     * @param wordGroup 分组
     * @param bytes byte[]
     * @param extraWords 词库中不存在的单词
     */
    Group updateFrequencyGroupTxt(Group wordGroup, byte[] bytes, List<String> extraWords);

    /**
     * 更新词频分组 (DOC)
     * @param wordGroup 分组
     * @param bytes byte[]
     * @param extraWords 词库中不存在的单词
     */
    Group updateFrequencyGroupDoc(Group wordGroup, byte[] bytes, List<String> extraWords);
}
