package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Set;

/**
 * 短语实体类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-5 21:10:34
 */
@Entity
@Table(name = "dict_phrase", uniqueConstraints = @UniqueConstraint(name = "uk_phrase", columnNames = {"library", "prototype"}))
@Getter
@Setter
public class Phrase extends BaseElement {

    private static final long serialVersionUID = 5771542562398020839L;

    /** 已掌握该短语的学生 */
    @ManyToMany(mappedBy = "masteredPhrases")
    protected Set<Student> masteredStudents;
}
