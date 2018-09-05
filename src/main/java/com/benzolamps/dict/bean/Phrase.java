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
 * 短语实体类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-5 21:10:34
 */
@Entity
@Table(name = "dict_phrase", uniqueConstraints = @UniqueConstraint(name = "phrase_unique", columnNames = {"library", "prototype"}))
@DetectColumnNum(3)
@Getter
@Setter
public class Phrase extends BaseElement {

    private static final long serialVersionUID = 5771542562398020839L;

    /** 已掌握的学生 */
    @ManyToMany(mappedBy = "masteredPhrases")
    protected Set<Student> masteredStudents;
}
