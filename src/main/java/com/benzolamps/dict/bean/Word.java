package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Set;

/**
 * 单词实体类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-5 21:10:34
 */
@Entity
@Table(name = "dict_word", uniqueConstraints = @UniqueConstraint(name = "uk_word", columnNames = {"library", "prototype"}))
@Getter
@Setter
@NoArgsConstructor
public class Word extends BaseElement {

    private static final long serialVersionUID = -7799252559204665509L;

    /** 美式发音 */
    @Convert(converter = FullWidthToHalfWidthConverter.class)
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String americanPronunciation;

    /** 英式发音 */
    @Convert(converter = FullWidthToHalfWidthConverter.class)
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String britishPronunciation;

    /** 词性 */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dict_wc", joinColumns = @JoinColumn(name = "word"), inverseJoinColumns = @JoinColumn(name = "clazz"))
    private Set<WordClazz> clazzes;

    /** 已掌握该单词的学生 */
    @ManyToMany(mappedBy = "masteredWords")
    private Set<Student> masteredStudents;

    /** 未掌握该单词的学生 */
    @ManyToMany(mappedBy = "failedWords")
    private Set<Student> failedStudents;

    /** 已掌握该单词的学生数 */
    @Formula("(select count(1) from dict_sw as sw where sw.word = id)")
    private Integer masteredStudentsCount;

    /** 未掌握该单词的学生数 */
    @Formula("(select count(1) from dict_swf as swf where swf.word = id)")
    private Integer failedStudentsCount;
}
