package com.benzolamps.dict.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.benzolamps.dict.util.DictLambda.tryFunc;
import static com.benzolamps.dict.util.DictSpring.getBean;

/**
 * 分组日志
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-10-1 00:00:10
 */
@Getter
@Setter
public class GroupLog implements Serializable {

    private static final long serialVersionUID = 367848676702331563L;

    /** 学生统计 */
    private List<Student> students = new ArrayList<>();

    /** 单词统计 */
    private List<Word> words = new ArrayList<>();

    /** 短语统计 */
    private List<Phrase> phrases = new ArrayList<>();

    @Converter
    public static class GroupLogConverter implements AttributeConverter<GroupLog, String> {

        @Override
        public String convertToDatabaseColumn(GroupLog value) {
            if (null == value) return null;
            return tryFunc(() -> getBean(ObjectMapper.class).writeValueAsString(value));
        }

        @Override
        public GroupLog convertToEntityAttribute(String value) {
            if (!StringUtils.hasText(value)) return null;
            return tryFunc(() -> getBean(ObjectMapper.class).readValue(value, GroupLog.class));
        }
    }
}
