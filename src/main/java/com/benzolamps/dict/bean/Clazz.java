package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DictIgnore;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictTextArea;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * 班级实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-15 14:47:54
 */
@Entity
@Table(name = "dict_class")
@Getter
@Setter
@JsonIgnoreProperties({"createDate", "modifyDate", "version", "remark"})
public class Clazz extends BaseEntity {

    private static final long serialVersionUID = 5117865263073835548L;

    /** 名称 */
    @Column(nullable = false)
    @NotEmpty
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$")
    @Length(max = 20)
    @DictPropertyInfo(display = "名称")
    private String name;

    /** 描述 */
    @Column
    @Length(max = 50)
    @DictTextArea
    @DictPropertyInfo(display = "描述")
    private String description;

    /** 学生 */
    @OneToMany(mappedBy = "clazz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @DictIgnore
    @JsonIgnore
    private Set<Student> students;

    /** 学生数 */
    @Formula("(select count(1) from dict_student as s where s.class = id)")
    @DictIgnore
    @JsonProperty("students")
    private Integer studentsCount;
}
