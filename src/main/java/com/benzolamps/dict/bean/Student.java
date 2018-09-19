package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DictRemote;
import com.benzolamps.dict.component.Size;
import com.benzolamps.dict.util.DictIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
    @Column(nullable = false, updatable = false, unique = true)
    @NotEmpty
    @Length(max = 20)
    @DictRemote("student/number_exists.json")
    @Min(1)
    private Integer number;

    /** 姓名 */
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 20)
    private String name;

    /** 描述 */
    @Column
    @Length(max = 50)
    private String description;

    /** 班级 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class", nullable = false)
    private Clazz clazz;

    /** 已掌握的单词 */
    @ManyToMany
    @DictIgnore
    @JsonIgnore
    @JoinTable(name = "dict_sw", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "word"))
    private Set<Word> masteredWords;

    /** 已掌握的短语 */
    @ManyToMany
    @DictIgnore
    @JsonIgnore
    @JoinTable(name = "dict_sp", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    private Set<Phrase> masteredPhrases;

    @Transient
    @Size("masteredWords")
    @DictIgnore
    private Integer masteredWordsCount;

    @Transient
    @Size("masteredPhrases")
    @DictIgnore
    private Integer masteredPhrasesCount;
}
