package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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
@Alias("groups")
public class Group extends BaseEntity {

    private static final long serialVersionUID = -756587292984332161L;

    /** 名称 */
    @NotEmpty
    @Length(max = 20)
    @Column(nullable = false, unique = true)
    @DictRemote("/word_group/name_not_exists.json")
    @DictPropertyInfo(display = "名称")
    private String name;

    /** 描述 */
    @Length(max = 50)
    @Column
    @DictPropertyInfo(display = "描述")
    private String description;

    /** 词库 */
    @ManyToOne
    @JoinColumn(name = "library", updatable = false, nullable = false)
    @DictIgnore
    @JsonIgnore
    private Library library;

    /** 状态 */
    @AllArgsConstructor
    public enum Status {
        /** 正常状态, 此状态可以导出单词, 可以删除, 可以给学生评分, 若已有评分, 则自动进入SCORING状态 */
        NORMAL("正常"),

        /**
         * 评分状态, 此状态可以导出单词, 但不可删除, 可以终止评分, 进入COMPLETED状态
         * 终止后未评分的学生的单词将不会做改动, 已评分才会做改动,
         * 评分过的学生评分记录会被保留, 不能再次评分
         * 所有学生全部评分完毕后, 自动进入COMPLETED状态
         */
        SCORING("评分中"),

        /**
         * 已完成状态, 可以导出, 可以查看单词与学生掌握情况
         * 可以转换为正常状态, 所有学生都可以重新评分
         */
        COMPLETED("已完成");

        private String name;

        @JsonValue
        public String getName() {
            return name;
        }
    }

    /** 状态 */
    @DictIgnore
    @Column(nullable = false)
    private Status status;

    /** 类型 */
    public enum Type {
        /** 单词 */
        WORD,

        /** 短语 */
        PHRASE
    }

    /** 类型 */
    @Column(nullable = false, updatable = false)
    @DictIgnore
    @JsonIgnore
    private Type type;

    /** 分组中的的学生 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gs", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "student"))
    @DictIgnore
    @JsonIgnore
    private Set<Student> studentsOriented;

    /** 已评分的学生 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gss", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "student"))
    @DictIgnore
    @JsonIgnore
    private Set<Student> studentsScored;

    /** 分组中的单词 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gw", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "word"))
    @DictIgnore
    @JsonIgnore
    @OrderBy("index")
    private Set<Word> words;

    /** 分组中的短语 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gp", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    @DictIgnore
    @JsonIgnore
    @OrderBy("index")
    private Set<Phrase> phrases;

    /** 分组日志 */
    @Convert(converter = GroupLog.GroupLogConverter.class)
    @Basic(fetch = FetchType.LAZY)
    @Column(insertable = false, length = 1000, columnDefinition = "longtext")
    @DictIgnore
    @JsonIgnore
    private GroupLog groupLog;

    /** 评分次数 */
    @Column(nullable = false)
    @DictIgnore
    private Integer scoreCount;

    /** 分组中的学生数 */
    @Formula("(select count(1) from dict_gs as gs where gs.groups = id)")
    @DictIgnore
    @JsonProperty("studentsOriented")
    private Integer studentsOrientedCount;

    /** 已评分的学生数 */
    @Formula("(select count(1) from dict_gss as gss where gss.groups = id)")
    @DictIgnore
    @JsonProperty("studentsScored")
    private Integer studentsScoredCount;

    /** 分组中的单词数 */
    @Formula("(select count(1) from dict_gw as gw where gw.groups = id)")
    @DictIgnore
    @JsonProperty("words")
    private Integer wordsCount;

    /** 分组中的短语数 */
    @Formula("(select count(1) from dict_gp as gp where gp.groups = id)")
    @DictIgnore
    @JsonProperty("phrases")
    private Integer phrasesCount;
}
