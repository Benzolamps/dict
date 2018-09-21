package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DictRemote;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 单词短语分组
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-15 14:29:16
 */
@Getter
@Setter
@Entity
@Table(name = "dict_group")
public class Group extends BaseEntity {

    private static final long serialVersionUID = -756587292984332161L;

    /** 名称 */
    @NotEmpty
    @Length(max = 20)
    @Column(nullable = false, unique = true)
    @DictRemote("/word_group/name_not_exists.json")
    private String name;

    /** 描述 */
    @Length(max = 50)
    @Column
    private String description;

    /** 词库 */
    @ManyToOne
    @JoinColumn(name = "library", updatable = false)
    private Library library;

    /** 状态 */
    public enum Status {
        /** 正常状态, 此状态可以导出单词, 可以删除, 可以给学生评分, 若已有评分, 则自动进入SCORING状态 */
        NORMAL,

        /**
         * 评分状态, 此状态可以导出单词, 但不可删除, 可以终止评分, 进入COMPLETED状态
         * 终止后未评分的学生的单词将不会做改动, 已评分才会做改动,
         * 评分过的学生评分记录会被保留, 不能再次评分
         * 所有学生全部评分完毕后, 自动进入COMPLETED状态
         */
        SCORING,

        /**
         * 已完成状态, 可以导出, 也可以删除, 可以查看单词与学生掌握情况
         * 可以转换为正常状态, 所有学生都可以重新评分
         */
        COMPLETED
    }

    /** 状态 */
    @Column(nullable = false)
    private Status status;

    /** 是否针对全体学生 */
    @Column(nullable = false)
    private Boolean isOrientedAllStudents;

    /** 针对的学生 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gs", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "student"))
    private Set<Student> studentsOriented;

    /** 针对的学生 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gc", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "clazz"))
    private Set<Student> clazzesOriented;

    /** 已评分的学生 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gss", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "student"))
    private Set<Student> studentsScored;

    /** 类型 */
    public enum Type {
        /** 单词 */
        WORD,

        /** 短语 */
        PHRASE
    }

    /** 类型 */
    @Column(nullable = false, updatable = false)
    @NotNull
    private Type type;

    /** 分组中的单词 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gw", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "word"))
    private Set<Phrase> phrases;

    /** 分组中的短语 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gp", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    private Set<Phrase> words;
}
