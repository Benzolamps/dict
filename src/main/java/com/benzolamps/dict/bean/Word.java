package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DetectColumnNum;
import com.benzolamps.dict.component.ExcelHeader;
import com.benzolamps.dict.component.Format;
import lombok.Getter;
import lombok.Setter;
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
@Table(name = "dict_word")
@DetectColumnNum(6)
@Getter
@Setter
public class Word extends BaseElement {

    private static final long serialVersionUID = -7799252559204665509L;

    /** 单词原形 */
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String prototype;

    /** 美式发音 */
    @Format("replaceString")
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String americanPronunciation;

    /** 英式发音 */
    @Format("replaceString")
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String britishPronunciation;

    /** 词性 */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dict_wc", joinColumns = @JoinColumn(name = "word"), inverseJoinColumns = @JoinColumn(name = "clazz"))
    private Set<WordClazz> clazzes;

    /** 词义 */
    @Format("replaceString")
    @Column
    @NotEmpty
    @Length(max = 255)
    private String definition;

    /** 已掌握的学生 */
    @ManyToMany(mappedBy = "masteredWords")
    protected Set<Student> masteredStudents;
}
