package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 词库实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-6-30 23:30:09
 */
@Entity
@Table(name = "dict_library")
@Getter
@Setter
public class Library extends BaseEntity {

    private static final long serialVersionUID = -8525365652508791389L;

    /** 名字 */
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 20)
    private String name;

    /** 描述 */
    @Column
    @Length(max = 50)
    private String description;

    /** 词库中的单词 */
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private Set<Word> words;

    /** 词库中的短语 */
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private Set<Phrase> phrases;

    /** 正在学习该词库的学生 */
    @OneToMany(mappedBy = "major")
    private Set<Student> students;
}
