package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyProcess;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;

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

    @Override
    @Transactional(readOnly = true)
    public boolean numberExists(Integer number) {
        return studentDao.count(Filter.eq("number", number)) > 0;
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

    @Override
    public StudyProcess[] getStudyProcess(Student student) {
        return studentDao.getStudyProcess(student);
    }
}
