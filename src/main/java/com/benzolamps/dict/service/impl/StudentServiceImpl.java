package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.service.base.StudentService;
import org.springframework.stereotype.Service;

/**
 * 学生Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:13:26
 */
@Service("studentService")
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService {
}
