package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;

/**
 * 单词短语分组Service接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 23:00:04
 */
public interface GroupService extends BaseService<Group> {

    /**
     * 判断名称是否存在
     * @param name 名称
     * @return 判断结果
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean nameExists(String name);

    /**
     * 添加学生
     * @param students 学生
     */
    void addStudents(Group group, Student... students);

    /**
     * 添加班级
     * @param clazzes 班级
     */
    void addClazzes(Group group, Clazz... clazzes);
}
