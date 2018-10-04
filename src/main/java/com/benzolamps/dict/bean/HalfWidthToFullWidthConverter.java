package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.Constant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 半角转全角字符
 * @author Benzolamps
 * @version 2.1.6
 * @datetime 2018-10-4 11:09:54
 */
@Converter
public class HalfWidthToFullWidthConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String value) {
        AtomicReference<String> aa = new AtomicReference<>(value);
        Constant.HALF_WIDTH_FULL_WIDTH_MAPPING.forEach(p -> aa.set(aa.get().replace(p.getKey(), p.getValue())));
        return aa.get();
    }

    @Override
    public String convertToEntityAttribute(String value) {
        AtomicReference<String> aa = new AtomicReference<>(value);
        Constant.HALF_WIDTH_FULL_WIDTH_MAPPING.forEach(p -> aa.set(aa.get().replace(p.getKey(), p.getValue())));
        return aa.get();
    }
}
