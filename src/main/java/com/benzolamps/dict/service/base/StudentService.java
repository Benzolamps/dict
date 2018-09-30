package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;

import java.util.Collection;

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

    void addMasteredWords(Student student, Word... words);

    void addFailedWords(Student student, Word... words);

    void addMasteredPhrases(Student student, Phrase... phrases);

    void addFailedPhrases(Student student, Phrase... phrases);
}
