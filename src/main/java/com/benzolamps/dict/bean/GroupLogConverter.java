package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.DictSpring;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 分组日志转换器
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-10-1 00:20:57
 */
@Converter
public class GroupLogConverter implements AttributeConverter<GroupLog, String> {

    @Override
    public String convertToDatabaseColumn(GroupLog attribute) {
        if (null == attribute) return null;
        return tryFunc(() -> DictSpring.getBean(ObjectMapper.class).writeValueAsString(attribute));
    }

    @Override
    public GroupLog convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasText(dbData)) return null;
        return tryFunc(() -> DictSpring.getBean(ObjectMapper.class).readValue(dbData, GroupLog.class));
    }
}