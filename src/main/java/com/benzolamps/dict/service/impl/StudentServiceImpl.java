package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
}
