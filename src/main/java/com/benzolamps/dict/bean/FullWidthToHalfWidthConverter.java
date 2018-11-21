package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.KeyValuePairs;

import javax.persistence.Converter;

import static com.benzolamps.dict.util.Constant.HALF_WIDTH_FULL_WIDTH_MAPPING;

/**
 * 全角字符转半角字符
 * @author Benzolamps
 * @version 2.1.6
 * @datetime 2018-10-4 11:08:54
 */
@Converter
public class FullWidthToHalfWidthConverter extends StringByteArrayConverter {
    @Override
    public String convertToDatabaseColumn0(String value) {
        if (value == null) {
            return null;
        }
        for (KeyValuePairs<Character, Character> p : HALF_WIDTH_FULL_WIDTH_MAPPING) {
            value = value.replace(p.getValue(), p.getKey());
        }
        return value;
    }

    @Override
    public String convertToEntityAttribute0(String value) {
        if (value == null) {
            return null;
        }
        for (KeyValuePairs<Character, Character> p : HALF_WIDTH_FULL_WIDTH_MAPPING) {
            value = value.replace(p.getValue(), p.getKey());
        }
        return value;
    }
}
