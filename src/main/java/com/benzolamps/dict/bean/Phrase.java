package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DetectColumnNum;
import com.benzolamps.dict.component.ExcelHeader;
import com.benzolamps.dict.component.Format;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * 短语实体类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-5 21:10:34
 */
@Entity
@Table(name = "dict_phrase")
@DetectColumnNum(3)
@Getter
@Setter
public class Phrase extends BaseElement {

    private static final long serialVersionUID = 5771542562398020839L;

    /** 短语原形 */
    @ExcelHeader(value = 1, notEmpty = true)
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String prototype;

    /** 词义 */
    @Format("replaceString")
    @ExcelHeader(value = 2, notEmpty = true)
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String definition;

    /** 已掌握的学生 */
    @ManyToMany(mappedBy = "masteredPhrases")
    protected Set<Student> masteredStudents;
}
