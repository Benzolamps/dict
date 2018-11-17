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
     *
     * @param phraseGroup 短语分组
     * @param phrases 短语
     */
    void removePhrases(Group phraseGroup, Phrase... phrases);

    /**
     * 评分
     * @param phraseGroup 短语分组
     * @param student 学生
     * @param phrases 已掌握的短语
     */
    void scorePhrases(Group phraseGroup, Student student, Phrase... phrases);

    /**
     * 导入学习进度
     * @param processImportVos ProcessImportVo
     */
    void importPhrases(ProcessImportVo... processImportVos);

    /**
     * 创建派生分组
     * @param original 原分组
     * @param phrases 短语集合
     * @param students 学生集合
     * @param phraseGroup 分组
     * @return 派生分组
     */
    Group extractDeriveGroup(Group original, Collection<Phrase> phrases, Collection<Student> students, Group phraseGroup);

    /**
     * 创建派生分组
     * @param original 原分组
     * @param students 学生集合
     * @param phraseGroup 分组
     * @return 专属分组集合
     */
    Collection<Group> extractPersonalGroup(Group original, Collection<Student> students, Group phraseGroup);

    /**
     * 添加词频分组 (TXT)
     * @param phraseGroup 分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     */
    Group persistFrequencyGroupTxt(Group phraseGroup, byte[] bytes, List<String> extraPhrases);

    /**
     * 添加词频分组 (DOC)
     * @param phraseGroup 分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     */
    Group persistFrequencyGroupDoc(Group phraseGroup, byte[] bytes, List<String> extraPhrases);

    /**
     * 更新词频分组 (TXT)
     * @param phraseGroup 分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     */
    Group updateFrequencyGroupTxt(Group phraseGroup, byte[] bytes, List<String> extraPhrases);

    /**
     * 更新词频分组 (DOC)
     * @param phraseGroup 分组
     * @param bytes byte[]
     * @param extraPhrases 词库中不存在的短语
     */
    Group updateFrequencyGroupDoc(Group phraseGroup, byte[] bytes, List<String> extraPhrases);
}
