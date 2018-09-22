package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.Alias;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictRemote;
import com.benzolamps.dict.component.Size;
import com.benzolamps.dict.util.DictIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
    @JoinColumn(name = "library", updatable = false)
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
         * 已完成状态, 可以导出, 也可以删除, 可以查看单词与学生掌握情况
         * 可以转换为正常状态, 所有学生都可以重新评分
         */
        COMPLETED("已完成");

        private String name;

        @Override
        public String toString() {
            return name;
        }
    }

    /** 状态 */
    @Column(nullable = false)
    @DictIgnore
    private Status status;

    /** 类型 */
    @AllArgsConstructor
    public enum Type {
        /** 单词 */
        WORD("单词"),

        /** 短语 */
        PHRASE("短语");

        private String name;

        @Override
        public String toString() {
            return name;
        }
    }

    /** 类型 */
    @Column(nullable = false, updatable = false)
    @DictIgnore
    @JsonIgnore
    private Type type;

    /** 分组中的学生 */
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

    /** 分组中的短语 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gp", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    @DictIgnore
    @JsonIgnore
    private Set<Phrase> words;

    /** 分组中的单词 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_gw", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "word"))
    @DictIgnore
    @JsonIgnore
    private Set<Phrase> phrases;

    /** 分组中的学生数 */
    @Transient
    @Size("studentsOriented")
    @DictIgnore
    @JsonProperty("studentsOriented")
    private Integer studentsOrientedCount;

    /** 已评分的学生数 */
    @Transient
    @Size("studentsScored")
    @DictIgnore
    @JsonProperty("studentsScored")
    private Integer studentsScoredCount;

    /** 单词数 */
    @Transient
    @Size("words")
    @DictIgnore
    @JsonProperty("words")
    private Integer wordsCount;

    /** 短语数 */
    @Transient
    @Size("phrases")
    @DictIgnore
    @JsonProperty("phrases")
    private Integer phrasesCount;
}
