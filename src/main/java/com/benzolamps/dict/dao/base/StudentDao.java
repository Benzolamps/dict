package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.vo.StudyProcessVo;

/**
 * 学生bDao接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:04:08
 */
public interface StudentDao extends BaseDao<Student> {

    /**
     * 获取单词学习进度
     * @param student 学生
     * @return 学习进度
     */
    StudyProcessVo getWordStudyProcess(Student student);

    /**
     * 获取短语学习进度
     * @param student 学生
     * @return 学习进度
     */
    StudyProcessVo getPhraseStudyProcess(Student student);
}
