package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * 词库实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-6-30 23:30:09
 */
@Entity
@Table(name = "dict_library")
@Getter
@Setter
public class Library extends BaseEntity {

    private static final long serialVersionUID = -8525365652508791389L;

    /** 名字 */
    @Column(nullable = false, unique = true)
    @NotEmpty
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$")
    @Length(max = 20)
    @DictRemote("/library/name_not_exists.json")
    @DictPropertyInfo(display = "名称")
    private String name;

    /** 描述 */
    @Column
    @Length(max = 50)
    @DictTextArea
    @DictPropertyInfo(display = "描述")
    private String description;

    /** 词库中的单词 */
    @JsonIgnore
    @DictIgnore
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private Set<Word> words;

    /** 词库中的短语 */
    @JsonIgnore
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    @DictIgnore
    private Set<Phrase> phrases;

    /** 单词的总数 */
    @Transient
    @Size("words")
    @DictIgnore
    @JsonProperty("words")
    private Integer wordsCount;

    /** 短语的总数 */
    @Transient
    @Size("phrases")
    @DictIgnore
    @JsonProperty("phrases")
    private Integer phrasesCount;
}
