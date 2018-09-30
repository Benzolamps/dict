package com.benzolamps.dict.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分组日志
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-10-1 00:00:10
 */
@Data
public class GroupLog implements Serializable {

    private static final long serialVersionUID = 367848676702331563L;

    /** 学生统计 */
    private List<Student> students = new ArrayList<>();

    /** 单词统计 */
    private List<Word> words = new ArrayList<>();

    /** 短语统计 */
    private List<Phrase> phrases = new ArrayList<>();
}
