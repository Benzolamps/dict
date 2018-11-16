package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictRemote;
import com.benzolamps.dict.service.base.ClazzService;
import com.benzolamps.dict.component.DictIgnore;
import com.benzolamps.dict.util.DictObject;
import com.benzolamps.dict.util.DictSpring;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生Vo
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-20 19:29:09
 */
@Data
public class StudentVo implements Serializable {

    private static final long serialVersionUID = 6388860303340465680L;

    /** id */
    @Id
    private Integer id;

    /** 学号 */
    @NotNull
    @DictRemote("/student/number_not_exists.json")
    @DictPropertyInfo(display = "学号")
    @Min(1)
    private Integer number;

    /** 姓名 */
    @NotEmpty
    @Length(max = 20)
    @DictPropertyInfo(display = "姓名")
    private String name;

    /** 描述 */
    @Length(max = 50)
    @DictPropertyInfo(display = "描述")
    private String description;

    /** 班级 */
    @NotNull
    @DictPropertyInfo(display = "班级")
    private Object clazz;

    /** 已掌握的单词数 */
    @DictIgnore
    @JsonProperty("masteredWords")
    private Integer masteredWordsCount;

    /** 已掌握的短语数 */
    @DictIgnore
    @JsonProperty("masteredPhrases")
    private Integer masteredPhrasesCount;
    
    /** 已掌握的单词数 */
    @DictIgnore
    @JsonProperty("failedWords")
    private Integer failedWordsCount;

    /** 已掌握的短语数 */
    @DictIgnore
    @JsonProperty("failedPhrases")
    private Integer failedPhrasesCount;

    /**
     * 将StudentVo转换为Student
     * @param studentVo studentVo
     * @return student
     */
    public static Student convertToStudent(StudentVo studentVo) {
        Assert.notNull(studentVo, "student vo不能为null");
        Integer clazzId = DictObject.ofObject(studentVo.getClazz(), int.class);
        Assert.notNull(clazzId, "clazz不能为null");
        Student student = new Student();
        student.setId(studentVo.getId());
        student.setNumber(studentVo.getNumber());
        student.setName(studentVo.getName());
        student.setDescription(studentVo.getDescription());
        ClazzService clazzService = DictSpring.getBean(ClazzService.class);
        Clazz clazz = clazzService.find(clazzId);
        Assert.notNull(clazz, "clazz不存在");
        student.setClazz(clazz);
        return student;
    }

    /**
     * 将Student转换为StudentVo
     * @param student student
     * @return studentVo
     */
    public static StudentVo convertFromStudent(Student student) {
        Assert.notNull(student, "student不能为null");
        StudentVo studentVo = new StudentVo();
        studentVo.setId(student.getId());
        studentVo.setNumber(student.getNumber());
        studentVo.setName(student.getName());
        studentVo.setDescription(student.getDescription());
        studentVo.setMasteredWordsCount(student.getMasteredWordsCount());
        studentVo.setMasteredPhrasesCount(student.getMasteredPhrasesCount());
        studentVo.setFailedWordsCount(student.getFailedWordsCount());
        studentVo.setFailedPhrasesCount(student.getFailedPhrasesCount());
        if (student.getClazz() != null) {
            studentVo.setClazz(student.getClazz().getId());
        }
        return studentVo;
    }
}
