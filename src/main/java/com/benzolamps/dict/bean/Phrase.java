package com.benzolamps.dict.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Set;

/**
 * 短语实体类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-5 21:10:34
 */
@SuppressWarnings("SqlResolve")
@Entity
@Table(name = "dict_phrase", uniqueConstraints = @UniqueConstraint(name = "uk_phrase", columnNames = {"library", "prototype"}))
@Getter
@Setter
public class Phrase extends BaseElement {

    private static final long serialVersionUID = 5771542562398020839L;

    /** 已掌握该短语的学生 */
    @ManyToMany(mappedBy = "masteredPhrases",cascade = CascadeType.ALL)
    private Set<Student> masteredStudents;

    /** 未掌握该短语的学生 */
    @ManyToMany(mappedBy = "failedPhrases", cascade = CascadeType.ALL)
    private Set<Student> failedStudents;

    /** 有该单短语的分组 */
    @ManyToMany(mappedBy = "phrases", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Group> groups;

    /** 已掌握该短语的学生数 */
    @Formula("(select count(1) from dict_sp as sp where sp.phrase = id)")
    private Integer masteredStudentsCount;

    /** 未掌握该短语的学生数 */
    @Formula("(select count(1) from dict_spf as spf where spf.phrase = id)")
    private Integer failedStudentsCount;
}
