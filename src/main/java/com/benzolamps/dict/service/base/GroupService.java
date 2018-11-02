package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;

import java.util.Set;

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
     * @param group 分组
     * @param students 学生
     */
    void addStudents(Group group, Student... students);

    /**
     * 添加班级
     * @param group 分组
     * @param clazzes 班级
     */
    void addClazzes(Group group, Clazz... clazzes);

    /**
     * 移除学生
     * @param group 分组
     * @param students 学生
     */
    void removeStudents(Group group, Student... students);

    /**
     * 跳过学生的评分
     * @param group 分组
     * @param student 学生
     */
    void jump(Group group, Student student);

    /**
     * 结束评分
     * @param group 分组
     */
    void finish(Group group);

    /**
     * 完成评分
     * @param group 分组
     */
    void complete(Group group);

    /**
     * 获取所有学生
     * @param group 分组
     * @return 学生
     */
    Set<Student> getStudentsOriented(Group group);
}
