package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.vo.ProcessImportVo;

import java.util.Collection;
import java.util.List;

/**
 * 短语分组Service接口
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
     * 移除短语
     * @param phraseGroup 短语分组
     * @param phrases 短语
     */
    void removePhrases(Group phraseGroup, Phrase... phrases);

    /**
     * 评分短语
     * @param phraseGroup 短语分组
     * @param student 学生
     * @param phrases 已掌握的短语
     */
    void scorePhrases(Group phraseGroup, Student student, Phrase... phrases);

    /**
     * 导入短语学习进度
     * @param processImportVos ProcessImportVo
     */
    void importPhrases(ProcessImportVo... processImportVos);

    /**
     * 创建派生短语分组
     * @param original 原短语分组
     * @param phrases 短语集合
     * @param students 学生集合
     * @param phraseGroup 短语分组
     * @return 派生短语分组
     */
    Group extractDeriveGroup(Group original, Collection<Phrase> phrases, Collection<Student> students, Group phraseGroup);

    /**
     * 创建专属短语分组
     * @param original 原短语分组
     * @param students 学生集合
     * @param phraseGroup 短语分组
     * @return 专属短语分组集合
     */
    Collection<Group> extractPersonalGroup(Group original, Collection<Student> students, Group phraseGroup);

    /**
     * 添加短语词频分组 (STR)
     * @param phraseGroup 短语分组
     * @param content 内容
     * @param extraPhrases 词库中不存在的短语
     * @return 添加后的分组
     */
    Group persistFrequencyGroupStr(Group phraseGroup, String content, List<String> extraPhrases);

    /**
     * 添加短语词频分组 (TXT)
     * @param phraseGroup 短语分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     * @return 添加后的分组
     */
    Group persistFrequencyGroupTxt(Group phraseGroup, byte[] bytes, List<String> extraPhrases);

    /**
     * 添加短语词频分组 (DOC)
     * @param phraseGroup 短语分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     * @return 添加后的分组
     */
    Group persistFrequencyGroupDoc(Group phraseGroup, byte[] bytes, List<String> extraPhrases);

    /**
     * 更新短语词频分组 (STR)
     * @param phraseGroup 短语分组
     * @param content 内容
     * @param extraPhrases 词库中不存在的短语
     * @return 更新后的分组
     */
    Group updateFrequencyGroupStr(Group phraseGroup, String content, List<String> extraPhrases);

    /**
     * 更新短语词频分组 (TXT)
     * @param phraseGroup 短语分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     * @return 更新后的分组
     */
    Group updateFrequencyGroupTxt(Group phraseGroup, byte[] bytes, List<String> extraPhrases);

    /**
     * 更新短语词频分组 (DOC)
     * @param phraseGroup 短语分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     * @return 更新后的分组
     */
    Group updateFrequencyGroupDoc(Group phraseGroup, byte[] bytes, List<String> extraPhrases);
}
