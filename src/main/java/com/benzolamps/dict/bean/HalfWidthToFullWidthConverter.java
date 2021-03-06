package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.KeyValuePairs;

import javax.persistence.Converter;

import static com.benzolamps.dict.util.Constant.HALF_WIDTH_FULL_WIDTH_MAPPING;

/**
 * 半角转全角字符
 * @author Benzolamps
 * @version 2.1.6
 * @datetime 2018-10-4 11:09:54
 */
@Converter
public class HalfWidthToFullWidthConverter extends StringByteArrayConverter {
    @Override
    public String parse(String value) {
        if (value == null) {
            return null;
        }
        for (KeyValuePairs<Character, Character> p : HALF_WIDTH_FULL_WIDTH_MAPPING) {
            value = value.replace(p.getKey(), p.getValue());
        }
        return value;
    }

    @Override
    public String dump(String value) {
        if (value == null) {
            return null;
        }
        for (KeyValuePairs<Character, Character> p : HALF_WIDTH_FULL_WIDTH_MAPPING) {
            value = value.replace(p.getKey(), p.getValue());
        }
        return value;
    }
}
