package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.*;
import com.benzolamps.dict.bean.Group.Status;
import com.benzolamps.dict.bean.Group.Type;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.GroupService;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.service.base.StudentService;
import com.benzolamps.dict.util.DictArray;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 单词短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:09:55
 */
public abstract class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

    private final Type type;

    @Resource
    private GroupDao groupDao;

    @Resource
    private LibraryService libraryService;

    @Resource
    private StudentService studentService;

    protected GroupServiceImpl(Type type) {
        Assert.notNull(type, "type不能为null");
        this.type = type;
    }

    @Override
    public Group find(Integer id) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        Group group = super.find(id);
        return library.equals(group.getLibrary()) && type.equals(group.getType()) ? group : null;
    }

    @Override
    public void persist(Group... groups) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        Arrays.stream(groups).forEach(group -> {
            group.setType(type);
            group.setStatus(Status.NORMAL);
            group.setLibrary(library);
        });
        super.persist(groups);
    }

    @Override
    public Group persist(Group group) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        group.setType(type);
        group.setStatus(Status.NORMAL);
        group.setLibrary(library);
        return super.persist(group);
    }

    @Override
    public void persist(Collection<Group> groups) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        groups.forEach(group -> {
            group.setType(type);
            group.setStatus(Status.NORMAL);
            group.setLibrary(library);
        });
        super.persist(groups);
    }

    @Override
    public Group update(Group group, String... ignoreProperties) {
        return super.update(group, DictArray.concat(ignoreProperties, new String[] {"type", "library"}));
    }

    @Override
    public void update(Collection<Group> groups, String... ignoreProperties) {
        super.update(groups, DictArray.concat(ignoreProperties, new String[] {"type", "library"}));
    }

    @Override
    protected void handlerFilter(Filter filter) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        filter.and(Filter.eq("type", type)).and(Filter.eq("library", library));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean nameExists(String name) {
        return groupDao.count(Filter.eq("name", name)) > 0;
    }

    @Transactional
    @Override
    public void addStudents(Group group, Student... students) {
        Assert.notNull(group, "group不能为null");
        Assert.isTrue(group.getType() == type, "group类型错误");
        group.getStudentsOriented().addAll(Arrays.stream(students).collect(Collectors.toSet()));
    }

    @Transactional
    @Override
    public void addClazzes(Group group, Clazz... clazzes) {
        Assert.notNull(group, "group不能为null");
        Assert.noNullElements(clazzes, "clazzes不能存在为null的元素");
        Assert.isTrue(group.getType() == type, "group类型错误");
        Set<Student> students = new HashSet<>();
        for (Clazz clazz : clazzes) {
            students.addAll(clazz.getStudents());
        }
        this.addStudents(group, students.toArray(new Student[0]));
    }

    @Transactional
    public void addWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == Type.WORD && wordGroup.getType() == type, "group类型错误");
        wordGroup.getWords().addAll(Arrays.stream(words).collect(Collectors.toSet()));
    }

    @Transactional
    public void addPhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == Type.PHRASE && phraseGroup.getType() == type, "group类型错误");
        phraseGroup.getPhrases().addAll(Arrays.stream(phrases).collect(Collectors.toSet()));
    }

    @Transactional
    public void removeWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == Type.WORD && wordGroup.getType() == type, "group类型错误");
        wordGroup.getWords().removeAll(Arrays.stream(words).collect(Collectors.toSet()));
    }

    @Transactional
    public void removePhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == Type.PHRASE && phraseGroup.getType() == type, "group类型错误");
        phraseGroup.getPhrases().removeAll(Arrays.stream(phrases).collect(Collectors.toSet()));
    }

    @Transactional
    public void scoreWords(Group wordGroup, Student student, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(words, "words不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == Type.WORD && wordGroup.getType() == type, "group类型错误");
        studentService.addFailedWords(student, wordGroup.getWords().toArray(new Word[0]));
        studentService.addMasteredWords(student, words);
        wordGroup.getStudentsScored().add(student);
        if (wordGroup.getStudentsOrientedCount() - wordGroup.getStudentsScoredCount() == 1) {
            wordGroup.setStatus(Status.COMPLETED);
        } else if (wordGroup.getStudentsScoredCount() == 0) {
            wordGroup.setStatus(Status.SCORING);
        }
    }

    @Transactional
    public void scorePhrases(Group phraseGroup, Student student, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(phrases, "phrases不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == Type.PHRASE && phraseGroup.getType() == type, "group类型错误");
        studentService.addFailedPhrases(student, phraseGroup.getPhrases().toArray(new Phrase[0]));
        studentService.addMasteredPhrases(student, phrases);
        phraseGroup.getStudentsScored().add(student);
        if (phraseGroup.getStudentsOrientedCount() - phraseGroup.getStudentsScoredCount() == 1) {
            phraseGroup.setStatus(Status.COMPLETED);
        } else if (phraseGroup.getStudentsScoredCount() == 0) {
            phraseGroup.setStatus(Status.SCORING);
        }
    }
}
