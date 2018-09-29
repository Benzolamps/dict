package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.util.Constant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *班级学生树Vo
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-9-29 20:57:46
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClazzStudentTreeVo implements Serializable {

    private static final long serialVersionUID = -5262819039218572314L;

    private Integer id;

    private String name;

    private String description;

    private List<StudentVo> students;

    /**
     * 将学生列表转换为班级学生树列表
     * @param students 学生列表
     * @return 班级学生树
     */
    @SuppressWarnings("unchecked")
    public static List<ClazzStudentTreeVo> convert(Collection<Student> students) {
        if (CollectionUtils.isEmpty(students)) {
            return Constant.EMPTY_LIST;
        } else {
            List<ClazzStudentTreeVo> clazzStudentTreeVos = new ArrayList<>();
            for (Student student : students) {
                Assert.notNull(student, "student不能为null");
                Assert.notNull(student.getClazz(), "clazz不能为null");
                StudentVo studentVo = new StudentVo();
                studentVo.setId(student.getId());
                studentVo.setName(student.getName());
                studentVo.setNumber(student.getNumber());
                studentVo.setDescription(student.getDescription());
                ClazzStudentTreeVo clazzStudentTreeVo = clazzStudentTreeVos.stream()
                        .filter(vo -> vo.getId().equals(student.getClazz().getId())).findFirst().orElse(null);
                if (clazzStudentTreeVo != null) {
                    clazzStudentTreeVo.students.add(studentVo);
                } else {
                    clazzStudentTreeVo = new ClazzStudentTreeVo();
                    clazzStudentTreeVo.setId(student.getClazz().getId());
                    clazzStudentTreeVo.setName(student.getClazz().getName());
                    clazzStudentTreeVo.setDescription(student.getClazz().getDescription());
                    clazzStudentTreeVo.students = new ArrayList<>();
                    clazzStudentTreeVo.students.add(studentVo);
                    clazzStudentTreeVos.add(clazzStudentTreeVo);
                }

            }
            return clazzStudentTreeVos;
        }
    }
}
