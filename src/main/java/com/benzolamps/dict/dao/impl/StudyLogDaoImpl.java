package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.StudyLog;
import com.benzolamps.dict.dao.base.StudyLogDao;
import org.springframework.stereotype.Repository;

/**
 * 学生学习记录Dao接口实现类
 * @author Benzolamps
 * @version 2.1.2
 * @datetime 2018-10-1 13:32:54
 */
@Repository("studyLogDao")
public class StudyLogDaoImpl extends BaseDaoImpl<StudyLog> implements StudyLogDao {
}
