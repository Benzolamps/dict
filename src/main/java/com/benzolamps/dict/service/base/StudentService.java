package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Student;

/**
 * 学生Service接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:12:22
 */
public interface StudentService extends BaseService<Student> {

    /**
     * 判断学号是否存在
     * @param number
     * @return 判断结果
     */
    boolean numberExists(Integer number);
}
