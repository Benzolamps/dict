package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.*;
import com.benzolamps.dict.bean.Group.Type;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.GroupService;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.service.base.StudentService;
import com.benzolamps.dict.service.base.StudyLogService;
import com.benzolamps.dict.util.DictArray;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

import static com.benzolamps.dict.bean.Group.Status.*;
import static com.benzolamps.dict.bean.Group.Type.WORD;

/**
 * 单词短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:09:55
 */
@Transactional
public abstract class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

    private final Type type;

    @Resource
    protected GroupDao groupDao;

    @Resource
    protected StudyLogService studyLogService;

    @Resource
    protected LibraryService libraryService;

    @Resource
    protected StudentService studentService;

    protected GroupServiceImpl(Type type) {
        Assert.notNull(type, "type不能为null");
        this.type = type;
    }

    @Override
    public Group find(Integer id) {
        Group group = super.find(id);
        return group != null && assertLibrary().equals(group.getLibrary()) && type.equals(group.getType()) ? group : null;
    }

    @Override
    public void persist(Group... groups) {
        Stream.of(groups).forEach(group -> {
            group.setType(type);
            group.setStatus(NORMAL);
            group.setLibrary(assertLibrary());
            group.setScoreCount(0);
            group.setFrequencyGenerated(false);
        });
        super.persist(groups);
    }

    @Override
    public Group persist(Group group) {
        group.setType(type);
        group.setStatus(NORMAL);
        group.setLibrary(assertLibrary());
        group.setScoreCount(0);
        group.setFrequencyGenerated(false);
        return super.persist(group);
    }

    @Override
    public void persist(Collection<Group> groups) {
        groups.forEach(group -> {
            group.setType(type);
            group.setStatus(NORMAL);
            group.setLibrary(assertLibrary());
            group.setScoreCount(0);
            group.setFrequencyGenerated(false);
        });
        super.persist(groups);
    }

    @Override
    public Group update(Group group, String... ignoreProperties) {
        Group ref = find(group.getId());
        if (!DictArray.contains(ignoreProperties, "name")) ref.setName(group.getName());
        if (!DictArray.contains(ignoreProperties, "description")) ref.setDescription(group.getDescription());
        return ref;
    }

    @Override
    public void update(Collection<Group> groups, String... ignoreProperties) {
        for (Group group : groups) {
            Group ref = find(group.getId());
            if (!DictArray.contains(ignoreProperties, "name")) ref.setName(group.getName());
            if (!DictArray.contains(ignoreProperties, "description")) ref.setDescription(group.getDescription());
        }
    }

    @Override
    public void remove(Collection<Group> groups) {
        Assert.isTrue(groups.stream().allMatch(Objects::nonNull), "groups中不能存在为null的元素");
        Assert.isTrue(groups.stream().allMatch(group -> group.getStatus() != SCORING), "无法删除正在评分的分组！");
        groups.stream().filter(Group::getFrequencyGenerated).forEach(this::clearFrequencyInfo);
        super.remove(groups);
    }

    /**
     * 清空词频信息
     * @param group 分组
     */
    protected void clearFrequencyInfo(Group group) {
        group.getElements().forEach(ele -> ele.getFrequencyInfo().removeIf(info -> info.getGroupId().equals(group.getId())));
    }

    @Override
    protected void handleFilter(final Filter filter) {
        filter.and(Filter.eq("type", type)).and(Filter.eq("library", assertLibrary()));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean nameExists(String name) {
        return groupDao.count(Filter.eq("name", name)) > 0;
    }

    @Transactional
    @Override
    public void addStudents(Group group, Student... students) {
        Assert.notNull(group, (WORD.equals(type) ? "word" : "phrase") + " group不能为null");
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
        Assert.notNull(group, (WORD.equals(type) ? "word" : "phrase") + " group不能为null");
        Assert.isTrue(group.getType() == type, "group类型错误");
        List<Student> studentList = Arrays.asList(students);
        group.getStudentsOriented().removeAll(studentList);
        group.getStudentsScored().removeAll(studentList);
        if (group.getGroupLog() != null) {
            group.getGroupLog().getStudents().removeAll(studentList);
        }
    }

    @Transactional
    @Override
    public void jump(Group group, Student student) {
        Assert.notNull(group, "group不能为null");
        Assert.notNull(student, "student不能为null");
        this.internalJump(group, student, null, null);
    }

    @SuppressWarnings("SameParameterValue")
    protected void internalJump(Group group, Student student, Integer wordsCount, Integer phrasesCount) {
        Assert.notNull(group, (WORD.equals(type) ? "word" : "phrase") + " group不能为null");
        Assert.notNull(student, "student不能为null");
        if (group.getStudentsOriented().size() - group.getStudentsScored().size() == 1) {
            group.setStatus(COMPLETED);
            group.setScoreCount(group.getScoreCount() + 1);
        } else if (group.getStudentsScored().isEmpty()) {
            group.setStatus(SCORING);
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
        studentReview.setMasteredWordsCount(wordsCount);
        studentReview.setMasteredPhrasesCount(phrasesCount);
        group.getGroupLog().getStudents().add(studentReview);

        if (wordsCount != null || phrasesCount != null) {
            StudyLog studyLog = new StudyLog();
            studyLog.setGroupName(group.getName());
            studyLog.setGroupCreateDate(group.getCreateDate());
            studyLog.setGroupType(group.getType());
            studyLog.setLibraryName(group.getLibrary().getName());
            studyLog.setLogDate(new Date());
            studyLog.setStudent(student);
            studyLog.setWordsCount(group.getWordsCount());
            studyLog.setMasteredWordsCount(wordsCount);
            studyLog.setPhrasesCount(group.getPhrasesCount());
            studyLog.setMasteredPhrasesCount(phrasesCount);
            studyLogService.persist(studyLog);
        }
    }

    @Transactional
    @Override
    public void finish(Group group) {
        Assert.notNull(group, "group不能为null");
        group.getStudentsOriented().stream()
            .filter(student -> !group.getStudentsScored().contains(student))
            .forEach(student -> this.internalJump(group, student, null, null));
    }

    @Transactional
    @Override
    public void complete(Group group) {
        Assert.notNull(group, "group不能为null");
        group.setStatus(NORMAL);
        group.setGroupLog(null);
        group.getStudentsScored().clear();
    }

    @Override
    public Map<String, Number> findMaxInfo() {
        return groupDao.findMaxInfo(assertLibrary());
    }

    /**
     * 断言分组和学生
     * @param group 分组
     * @param student 学生
     */
    protected void assertGroupAndStudent(Group group, Student student) {
        if (group != null) {
            Assert.isTrue(!Group.Status.COMPLETED.equals(group.getStatus()), group.getName() + " 分组当前处于已完成状态，无法进行评分！");
            if (student != null) {
                Assert.isTrue(group.getStudentsOriented().contains(student), student.getName() + " 不在 " + group.getName() + " 分组中！");
                Assert.isTrue(!group.getStudentsScored().contains(student), student.getName() + " 已评分 " + group.getName() + " 分组！");
            }
        }
    }

    /**
     * 断言是否选中词库
     * @return 当前词库
     */
    protected Library assertLibrary() {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        return library;
    }
}
