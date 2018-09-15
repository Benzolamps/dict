package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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
public class Clazz extends BaseEntity {

    @Column(nullable = false)
    @NotEmpty
    @Length(max = 20)
    private String name;

    @Column
    @Length(max = 50)
    private String description;

    /** 主要学习的词库 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "major", nullable = false)
    private Library major;

    @OneToMany(mappedBy = "clazz", fetch = FetchType.LAZY)
    private Set<Student> students;
}
