package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyLog;

import java.util.Collection;

/**
 * 学生学习记录Service接口
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-10-1 13:34:22
 */
public interface StudyLogService extends BaseService<StudyLog> {

    /**
     * 根据学生查找单词学习记录
     * @param student 学生
     * @return 学习记录集合
     */
    Collection<StudyLog> findWordLogs(Student student);

    /**
     * 根据学生查找短语学习记录
     * @param student 学生
     * @return 学习记录集合
     */
    Collection<StudyLog> findPhraseLogs(Student student);
}
