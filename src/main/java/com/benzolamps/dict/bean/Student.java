package com.benzolamps.dict.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 学生实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:29:33
 */
@SuppressWarnings("SqlResolve")
@Entity
@Table(name = "dict_student")
@Getter
@Setter
@JsonIgnoreProperties({"createDate", "modifyDate", "version", "remark"})
public class Student extends BaseEntity {

    private static final long serialVersionUID = -4963430274445493200L;

    /** 学号 */
    @Column(nullable = false, unique = true)
    @NotNull
    @Min(1)
    private Integer number;

    /** 姓名 */
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 20)
    private String name;

    /** 班级 */
    @ManyToOne
    @JoinColumn(name = "class", nullable = false)
    private Clazz clazz;

    /** 描述 */
    @Column
    @Length(max = 50)
    private String description;

    /** 已掌握的单词 */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dict_sw", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "word"))
    @JsonIgnore
    private Set<Word> masteredWords;

    /** 已掌握的短语 */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dict_sp", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    @JsonIgnore
    private Set<Phrase> masteredPhrases;

    /** 未掌握的单词 */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dict_swf", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "word"))
    @JsonIgnore
    private Set<Word> failedWords;

    /** 未掌握的短语 */
    @ManyToMany
    @JoinTable(name = "dict_spf", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    @JsonIgnore
    private Set<Phrase> failedPhrases;

    /** 有该学生的分组 */
    @ManyToMany(mappedBy = "studentsOriented", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Group> groupsOriented;

    /** 该学生已评分完毕的分组 */
    @ManyToMany(mappedBy = "studentsScored", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Group> groupsScored;

    /** 学习记录 */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<StudyLog> studyLogs;

    /** 已掌握的单词数 */
    @Formula("(select count(1) from dict_sw as sw where sw.student = id)")
    private Integer masteredWordsCount;

    /** 已掌握的短语数 */
    @Formula("(select count(1) from dict_sp as sp where sp.student = id)")
    private Integer masteredPhrasesCount;

    /** 未掌握的单词数 */
    @Formula("(select count(1) from dict_swf as swf where swf.student = id)")
    private Integer failedWordsCount;

    /** 未掌握的短语数 */
    @Formula("(select count(1) from dict_spf as spf where spf.student = id)")
    private Integer failedPhrasesCount;
}
