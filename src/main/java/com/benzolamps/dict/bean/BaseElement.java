package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.ExcelHeader;
import com.benzolamps.dict.component.Format;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 单词或短语类的基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-6-30 23:05:02
 */
@MappedSuperclass
@Setter
@Getter
public abstract class BaseElement extends BaseEntity {

    private static final long serialVersionUID = -3019993342620190686L;

    /** 索引 */
    @Column(nullable = false, updatable = false, name = "indexes")
    @NotNull
    protected Integer index;

    /** 原形 */
    @ExcelHeader(value = 1, notEmpty = true)
    @Column(nullable = false, updatable = false, unique = true)
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

    /** 词库 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "library", nullable = false, updatable = false)
    protected Library library;

    /* 已掌握该单词 (短语) 的学生 */
    @Transient
    protected Set<Student> masteredStudents;

    /** @return 用于实体类中字段的格式 */
    @SuppressWarnings("unused")
    public String replaceString(String str) {
        return str == null ? "" : str
            .replaceAll("[ \\s\\u00a0]+", "")
            .replace("（", "(")
            .replace("）", ")")
            .replace("：", ":")
            .replaceAll("[,，]+", "，")
            .replaceAll("[;；]+", "；")
            .replaceAll("(^；+)|(；+$)|(^，+)|(，+$)", "");
    }
}
