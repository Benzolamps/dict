package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyProcess;

import java.util.Map;

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

    /**
     * 通过学号查询学生
     * @param number 学号
     * @return 学生
     */
    Student findByNumber(Integer number);

    /**
     * 获取最大值信息
     * @return 最大值信息
     */
    Map<String, Number> findMaxInfo();
}
