package com.benzolamps.dict.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Set;

/**
 * 单词词性实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 00:22:55
 */
@Entity
@Table(name = "dict_word_clazz")
@Getter
@Setter
@JsonIgnoreProperties({"createDate", "modifyDate", "version", "remark"})
public class WordClazz extends BaseEntity {

    private static final long serialVersionUID = -7259857902911189092L;

    /** 名字 */
    @Column(nullable = false, unique = true)
    @NotEmpty
    @Length(max = 255)
    private String name;

    /** 描述 */
    @Column
    @Length(max = 255)
    private String description;

    /** 单词 */
    @ManyToMany(mappedBy = "clazzes", fetch = FetchType.LAZY)
    private Set<Word> words;
}
