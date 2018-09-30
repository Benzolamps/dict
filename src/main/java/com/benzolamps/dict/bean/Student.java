package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Entity
@Table(name = "dict_student")
@Getter
@Setter
@NoArgsConstructor
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
    @JoinTable(name = "dict_sw", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "word"))
    private Set<Word> masteredWords;

    /** 已掌握的短语 */
    @ManyToMany
    @JoinTable(name = "dict_sp", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    private Set<Phrase> masteredPhrases;

    /** 未掌握的单词 */
    @ManyToMany
    @JoinTable(name = "dict_swf", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "word"))
    private Set<Word> failedWords;

    /** 未掌握的短语 */
    @ManyToMany
    @JoinTable(name = "dict_spf", joinColumns = @JoinColumn(name = "student"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    private Set<Phrase> failedPhrases;

    /** 已掌握的单词数 */
    @Transient
    @Size("masteredWords")
    private Integer masteredWordsCount;

    /** 已掌握的短语数 */
    @Transient
    @Size("masteredPhrases")
    private Integer masteredPhrasesCount;

    /** 未掌握的单词数 */
    @Transient
    @Size("failedWords")
    private Integer failedWordsCount;

    /** 未掌握的短语数 */
    @Transient
    @Size("failedPhrases")
    private Integer failedPhrasesCount;

    public Student(Number number, String name, String description, Clazz clazz, Number masteredWordsCount, Number masteredPhrasesCount) {
        this.number = number.intValue();
        this.name = name;
        this.description = description;
        this.clazz = clazz;
        this.masteredWordsCount = masteredWordsCount.intValue();
        this.masteredPhrasesCount = masteredPhrasesCount.intValue();
    }
}
