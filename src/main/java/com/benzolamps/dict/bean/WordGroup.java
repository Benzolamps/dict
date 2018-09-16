package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 单词分组实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-15 15:02:55
 */
@Entity
@Table(name = "dict_word_group")
@Getter
@Setter
public class WordGroup extends BaseGroup {

    private static final long serialVersionUID = 6986324148442795620L;

    /** 分组中的单词 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_wg", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "word"))
    private Set<Word> words;


    /** 已对该分组评过分的学生 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_wgs", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "student"))
    private Set<Phrase> students;
}
