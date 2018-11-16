package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.service.base.PhraseGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:40:44
 */
@Slf4j
@Service("phraseGroupService")
@Transactional
public class PhraseGroupServiceImpl extends GroupServiceImpl implements PhraseGroupService {

    protected PhraseGroupServiceImpl() {
        super(Group.Type.PHRASE);
    }

    @Override
    @Transactional
    public void addPhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.isTrue(!phraseGroup.getFrequencyGenerated(), "词频分组不能添短语");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        phraseGroup.getPhrases().addAll(Arrays.asList(phrases));
    }

    @Override
    @Transactional
    public void removePhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        phraseGroup.getPhrases().removeAll(Arrays.asList(phrases));
        List<Phrase> phraseList = Arrays.asList(phrases);
        phraseList.stream().map(Phrase::getFrequencyInfo).forEach(infos -> infos.removeIf(info -> info.getGroupId().equals(phraseGroup.getId().toString())));
        phraseGroup.getPhrases().removeAll(phraseList);
        if (phraseGroup.getGroupLog() != null) {
            phraseGroup.getGroupLog().getPhrases().removeAll(phraseList);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    @Transactional
    public void scorePhrases(Group phraseGroup, Student student, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(phrases, "phrases不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        this.assertGroupAndStudent(phraseGroup, student);
        studentService.addFailedPhrases(student, phraseGroup.getPhrases().toArray(new Phrase[0]));
        studentService.addMasteredPhrases(student, phrases);
        this.internalJump(phraseGroup, student, null, phrases.length);
        for (Phrase phrase : phrases) {
            Phrase phraseReview = phraseGroup.getGroupLog().getPhrases().stream().filter(phrase::equals).findFirst().orElse(null);
            phraseReview.setMasteredStudentsCount(phraseReview.getMasteredStudentsCount() + 1);
        }
    }

    @Override
    @Transactional
    public void importPhrases(ProcessImportVo... processImportVos) {
        throw new UnsupportedOperationException("该功能尚未实现！");
    }

    @Override
    public Group extractDeriveGroup(Group original, Collection<Phrase> phrases, Collection<Student> students, Group phraseGroup) {
        Assert.isTrue(!(CollectionUtils.isEmpty(phrases) && CollectionUtils.isEmpty(students)), "words和students不能同时为null或空");
        /* lambda只能用final */
        Collection<?> phrases1 = phrases, students1 = students;
        /* 去除所有学生都会的单词 */
        if (CollectionUtils.isEmpty(phrases)) {
            Assert.notNull(original, "original不能为null");
            phrases = new HashSet<>(original.getPhrases());
            phrases.removeIf(phrase -> phrase.getMasteredStudents().containsAll(students1));
        }
        /* 去除所有单词都会的学生 */
        if (CollectionUtils.isEmpty(students)) {
            Assert.notNull(original, "original不能为null");
            students = new HashSet<>(original.getStudentsOriented());
            students.removeIf(student -> student.getMasteredPhrases().containsAll(phrases1));
        }
        phraseGroup.setPhrases(new HashSet<>(phrases));
        phraseGroup.setStudentsOriented(new HashSet<>(students));
        return persist(phraseGroup);
    }

    @Override
    public Collection<Group> extractPersonalGroup(Group original, Collection<Student> students, Group phraseGroup) {
        Assert.notNull(original, "original不能为null");
        Assert.notEmpty(students, "students不能为空");
        Collection<Group> groups = students.stream().map(student -> {
            Group group = new Group();
            String name = phraseGroup.getName() + " - " + student.getName();
            int i = 0;
            while (nameExists(name)) {
                name = phraseGroup.getName() + " - " + student.getName() + " - " + i++;
            }
            group.setName(name);
            group.setDescription(phraseGroup.getDescription());
            Set<Phrase> phrases = new HashSet<>(original.getPhrases());
            phrases.removeIf(student.getMasteredPhrases()::contains);
            group.setPhrases(phrases);
            group.setStudentsOriented(Collections.singleton(student));
            return group;
        }).collect(Collectors.toList());
        persist(groups);
        return groups;
    }

    @Override
    public Group persistFrequencyGroupTxt(Group phraseGroup, byte[] bytes, List<String> extraPhrases) {
        throw new UnsupportedOperationException("该功能尚未实现！");
    }

    @Override
    public Group persistFrequencyGroupDoc(Group phraseGroup, byte[] bytes, List<String> extraPhrases) {
        throw new UnsupportedOperationException("该功能尚未实现！");
    }
}
