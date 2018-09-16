package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 短语分组实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-15 15:02:27
 */
@Entity
@Table(name = "dict_phrase_group")
@Getter
@Setter
public class PhraseGroup extends BaseGroup {

    private static final long serialVersionUID = 8881615391449832778L;

    /** 分组中的短语 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_pg", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "phrase"))
    private Set<Phrase> phrases;

    /** 已对该分组评过分的学生 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dict_pgs", joinColumns = @JoinColumn(name = "groups"), inverseJoinColumns = @JoinColumn(name = "student"))
    private Set<Phrase> students;
}
