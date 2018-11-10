package com.benzolamps.dict.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.benzolamps.dict.util.DictSpring.getBean;

/**
 * 词频信息
 * @author Benzolamps
 * @version 2.2.9
 * @datetime 2018-11-10 23:16:04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyInfo {

    /** 分组id */
    private String groupId;

    /** 词频 */
    private Integer frequency;

    /** 词频信息转换 */
    @Converter
    public static class FrequencyInfoConverter implements AttributeConverter<List<FrequencyInfo>, String> {

        @Override
        @SneakyThrows(IOException.class)
        public String convertToDatabaseColumn(List<FrequencyInfo> value) {
            return CollectionUtils.isEmpty(value) ? null : getBean(ObjectMapper.class).writeValueAsString(value);
        }

        @SuppressWarnings("unchecked")
        @Override
        @SneakyThrows(IOException.class)
        public List<FrequencyInfo> convertToEntityAttribute(String value) {
            if (value == null) {
                return new ArrayList<>();
            }
            List<Map> list = getBean(ObjectMapper.class).readValue(value, List.class);
            List<FrequencyInfo> frequencyInfoList = new ArrayList<>();
            list.forEach(item -> frequencyInfoList.add(new FrequencyInfo((String) item.get("groupId"), (Integer) item.get("frequency"))));
            return frequencyInfoList;
        }
    }
}