package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.dao.base.StudentDao;
import org.springframework.stereotype.Repository;

/**
 * 学生Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:09:29
 */
@Repository("studentDao")
public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao {
}
