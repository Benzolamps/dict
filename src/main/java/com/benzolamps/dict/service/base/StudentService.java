package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.StudyProcess;

import java.util.Map;

/**
 * 学生Service接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:12:22
 */
public interface StudentService extends BaseService<Student> {

    /**
     * 判断学号是否存在
     * @param number 学号
     * @return 判断结果
     */
    boolean numberExists(Integer number);

    /**
     * 添加已掌握的单词
     * @param student 学生
     * @param words 单词
     */
    void addMasteredWords(Student student, Word... words);

    /**
     * 添加未掌握的单词
     * @param student 学生
     * @param words 单词
     */
    void addFailedWords(Student student, Word... words);

    /**
     * 添加已掌握的短语
     * @param student 学生
     * @param phrases 短语
     */
    void addMasteredPhrases(Student student, Phrase... phrases);

    /**
     * 添加未掌握的短语
     * @param student 学生
     * @param phrases 短语
     */
    void addFailedPhrases(Student student, Phrase... phrases);

    /**
     * 获取学习进度
     * @param student 学生
     * @return 学习进度
     */
    StudyProcess[] getStudyProcess(Student student);

    /**
     * 获取最大值信息
     * @return 最大值信息
     */
    Map<String, Number> findMaxInfo();
}
