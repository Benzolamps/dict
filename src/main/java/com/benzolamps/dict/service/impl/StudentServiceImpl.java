package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.*;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.base.StudyLogDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;

/**
 * 学生Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:13:26
 */
@Service("studentService")
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService {

    @Resource
    private StudentDao studentDao;

    @Resource
    private GroupDao groupDao;

    @Resource
    private StudyLogDao studyLogDao;

    @Override
    @Transactional(readOnly = true)
    public boolean numberExists(Integer number) {
        return studentDao.count(Filter.eq("number", number)) > 0;
    }

    @Override
    public void remove(Collection<Student> students) {
        /* 删除学生前删除与各个分组的关联 */
        Collection<Group> groups = groupDao.findList(new Filter());
        for (Group group : groups) {
            group.getStudentsOriented().removeAll(students);
            group.getStudentsScored().removeAll(students);
        }
        studyLogDao.remove(Filter.in("student", students));
        super.remove(students);
    }

    @Override
    @Transactional
    public void addMasteredWords(Student student, Word... words) {
        Assert.notNull(student, "student不能为null");
        Assert.notNull(words, "words不能为null");
        student.getMasteredWords().addAll(Arrays.asList(words));
        student.getFailedWords().removeAll(Arrays.asList(words));
    }

    @Override
    public void addFailedWords(Student student, Word... words) {
        Assert.notNull(student, "student不能为null");
        Assert.notNull(words, "words不能为null");
        student.getFailedWords().addAll(Arrays.asList(words));
        student.getMasteredWords().removeAll(Arrays.asList(words));
    }

    @Override
    public void addMasteredPhrases(Student student, Phrase... phrases) {
        Assert.notNull(student, "student不能为null");
        Assert.notNull(phrases, "phrases不能为null");
        student.getMasteredPhrases().addAll(Arrays.asList(phrases));
        student.getFailedPhrases().removeAll(Arrays.asList(phrases));
    }

    @Override
    public void addFailedPhrases(Student student, Phrase... phrases) {
        Assert.notNull(student, "student不能为null");
        Assert.notNull(phrases, "phrases不能为null");
        student.getFailedPhrases().addAll(Arrays.asList(phrases));
        student.getMasteredPhrases().removeAll(Arrays.asList(phrases));
    }
}
