package com.benzolamps.dict.bean;

import javax.persistence.AttributeConverter;

import static com.benzolamps.dict.util.Constant.UTF8_CHARSET;

/**
 * 字符串转byte数组
 * @author Benzolamps
 * @version 2.3.4
 * @datetime 2018-11-21 09:47:24
 */
public abstract class StringByteArrayConverter implements AttributeConverter<String, byte[]> {

    @Override
    public final byte[] convertToDatabaseColumn(String value) {
        if (value == null) return null;
        value = parse(value);
        return value.getBytes(UTF8_CHARSET);
    }

    @Override
    public final String convertToEntityAttribute(byte[] value) {
        if (value == null) return null;
        String str = new String(value, UTF8_CHARSET);
        str = dump(str);
        return str;
    }

    public abstract String parse(String value);

    public abstract String dump(String value);
}
