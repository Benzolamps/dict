package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyLog;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.service.base.StudyLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

import static com.benzolamps.dict.bean.Group.Type.PHRASE;
import static com.benzolamps.dict.bean.Group.Type.WORD;

/**
 * 学生学习记录Service接口实现类
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-10-1 22:03:13
 */
@Service("studyLogService")
public class StudyLogServiceImpl extends BaseServiceImpl<StudyLog> implements StudyLogService {

    @Override
    public void persist(Collection<StudyLog> studyLogs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void persist(StudyLog... studyLogs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StudyLog persist(StudyLog studyLog) {
        Group.Type groupType = studyLog.getGroupType();
        Student student = studyLog.getStudent();
        Filter filter = Filter.eq("groupType", groupType).and(Filter.eq("student", student));
        int count = count(filter);
        if (count >= 10) {
            List<StudyLog> removed = findCount(count - 9, filter, Order.asc("logDate"));
            this.remove(removed);
        }
        return super.persist(studyLog);
    }

    @Override
    public Collection<StudyLog> findWordLogs(Student student) {
        Assert.notNull(student, "student不能为null");
        return findList(Filter.eq("student", student).and(Filter.eq("groupType", WORD)), Order.asc("logDate"));
    }

    @Override
    public Collection<StudyLog> findPhraseLogs(Student student) {
        Assert.notNull(student, "student不能为null");
        return findList(Filter.eq("student", student).and(Filter.eq("groupType", PHRASE)), Order.asc("logDate"));
    }
}
