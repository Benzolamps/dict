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
 * 学生实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:29:33
 */
@Entity
@Table(name = "dict_student")
@Getter
@Setter
public class Student extends BaseEntity {

    private static final long serialVersionUID = -4963430274445493200L;

    /** 学号 */
    @Column(nullable = false, updatable = false)
    @NotEmpty
    @Length(max = 20)
    private String number;

    /** 姓名 */
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 20)
    private String name;

    /** 主要学习的词库 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "major")
    private Library major;

    /** 已掌握的单词 */
    @ManyToMany
    @JoinTable(name = "dict_sw", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "word"))
    private Set<Word> masteredWords;

    /** 已掌握的短语 */
    @ManyToMany
    @JoinTable(name = "dict_sp", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    private Set<Phrase> masteredPhrases;
}
