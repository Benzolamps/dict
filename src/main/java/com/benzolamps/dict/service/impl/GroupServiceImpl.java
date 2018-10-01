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
import java.util.*;

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
        group.getStudentsOriented().addAll(Arrays.asList(students));
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
    @Override
    public void removeStudents(Group group, Student... students) {
        Assert.notNull(group, "group不能为null");
        Assert.isTrue(group.getType() == type, "group类型错误");
        List<Student> studentList = Arrays.asList(students);
        group.getStudentsOriented().removeAll(studentList);
        group.getStudentsScored().removeAll(studentList);
        if (group.getGroupLog() != null) {
            group.getGroupLog().getStudents().removeAll(studentList);
        }
    }

    @Transactional
    public void addWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == Type.WORD && wordGroup.getType() == type, "group类型错误");
        wordGroup.getWords().addAll(Arrays.asList(words));
    }

    @Transactional
    public void addPhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == Type.PHRASE && phraseGroup.getType() == type, "group类型错误");
        phraseGroup.getPhrases().addAll(Arrays.asList(phrases));
    }

    @Transactional
    public void removeWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == Type.WORD && wordGroup.getType() == type, "group类型错误");
        List<Word> wordList = Arrays.asList(words);
        wordGroup.getWords().removeAll(wordList);
        if (wordGroup.getGroupLog() != null) {
            wordGroup.getGroupLog().getWords().removeAll(wordList);
        }

    }

    @Transactional
    public void removePhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == Type.PHRASE && phraseGroup.getType() == type, "group类型错误");
        phraseGroup.getPhrases().removeAll(Arrays.asList(phrases));
        List<Phrase> phraseList = Arrays.asList(phrases);
        phraseGroup.getPhrases().removeAll(phraseList);
        if (phraseGroup.getGroupLog() != null) {
            phraseGroup.getGroupLog().getPhrases().removeAll(phraseList);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Transactional
    public void scoreWords(Group wordGroup, Student student, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(words, "words不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == Type.WORD && wordGroup.getType() == type, "group类型错误");
        studentService.addFailedWords(student, wordGroup.getWords().toArray(new Word[0]));
        studentService.addMasteredWords(student, words);
        this.internalJump(wordGroup, student, words.length);
        for (Word word : words) {
            Word wordReview = wordGroup.getGroupLog().getWords().stream().filter(word::equals).findFirst().orElse(null);
            wordReview.setMasteredStudentsCount(wordReview.getMasteredStudentsCount() + 1);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Transactional
    public void scorePhrases(Group phraseGroup, Student student, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(phrases, "phrases不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == Type.PHRASE && phraseGroup.getType() == type, "group类型错误");
        studentService.addFailedPhrases(student, phraseGroup.getPhrases().toArray(new Phrase[0]));
        studentService.addMasteredPhrases(student, phrases);
        this.internalJump(phraseGroup, student, phrases.length);
        for (Phrase phrase : phrases) {
            Phrase phraseReview = phraseGroup.getGroupLog().getPhrases().stream().filter(phrase::equals).findFirst().orElse(null);
            phraseReview.setMasteredStudentsCount(phraseReview.getMasteredStudentsCount() + 1);
        }
    }

    @Transactional
    @Override
    public void jump(Group group, Student student) {
        Assert.notNull(group, "group不能为null");
        Assert.notNull(student, "student不能为null");
        internalJump(group, student, null);
    }

    private void internalJump(Group group, Student student, Integer count) {
        if (group.getStudentsOrientedCount() - group.getStudentsScoredCount() == 1) {
            group.setStatus(Status.COMPLETED);
        } else if (group.getStudentsScoredCount() == 0) {
            group.setStatus(Status.SCORING);
        }
        if (group.getGroupLog() == null) {
            group.setGroupLog(new GroupLog());
            for (Word word : group.getWords()) {
                Word wordReview = new Word();
                wordReview.setId(word.getId());
                wordReview.setIndex(word.getIndex());
                wordReview.setPrototype(word.getPrototype());
                wordReview.setDefinition(word.getDefinition());
                wordReview.setMasteredStudentsCount(0);
                group.getGroupLog().getWords().add(wordReview);
            }
            for (Phrase phrase : group.getPhrases()) {
                Phrase phraseReview = new Phrase();
                phraseReview.setId(phrase.getId());
                phraseReview.setIndex(phrase.getIndex());
                phraseReview.setPrototype(phrase.getPrototype());
                phraseReview.setDefinition(phrase.getDefinition());
                phraseReview.setMasteredStudentsCount(0);
                group.getGroupLog().getPhrases().add(phraseReview);
            }
        }
        group.getStudentsScored().add(student);
        Student studentReview = new Student();
        studentReview.setId(student.getId());
        studentReview.setNumber(student.getNumber());
        studentReview.setName(student.getName());
        studentReview.setDescription(student.getDescription());
        Clazz clazz = new Clazz();
        clazz.setId(student.getClazz().getId());
        clazz.setName(student.getClazz().getName());
        clazz.setDescription(student.getClazz().getDescription());
        studentReview.setClazz(clazz);
        studentReview.setMasteredWordsCount(count);
        group.getGroupLog().getStudents().add(studentReview);
    }

    @Transactional
    @Override
    public void finish(Group group) {
        Assert.notNull(group, "phrase group不能为null");
        group.setStatus(Status.COMPLETED);
        group.getStudentsScored().addAll(group.getStudentsOriented());
    }

    @Transactional
    @Override
    public void complete(Group group) {
        Assert.notNull(group, "phrase group不能为null");
        group.setStatus(Status.NORMAL);
        group.setGroupLog(null);
        group.getStudentsScored().clear();
    }
}
