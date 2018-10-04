package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyProcess;

/**
 * 学生bDao接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:04:08
 */
public interface StudentDao extends BaseDao<Student> {

    /**
     * 获取学习进度
     * @param student 学生
     * @return 学习进度
     */
    StudyProcess[] getStudyProcess(Student student);
}
