package com.benzolamps.dict.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.benzolamps.dict.util.Constant.UTF8_CHARSET;

/**
 * String工具类
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-9-29 17:25:57
 */
public interface DictString {

    /**
     * 将变量名转换为驼峰
     * @param origin 原形
     * @return 转换后
     */
    static String toCamel(String origin) {
        if (!StringUtils.hasText(origin)) return origin;
        StringBuilder sb = new StringBuilder();
        if (origin.contains("-") || origin.contains("_")) {
            origin = origin.toLowerCase();
            for (int i = 0; i < origin.length(); i++) {
                char chr = origin.charAt(i);
                if ((chr == '_' || chr == '-') && i + 1 < origin.length()) {
                    sb.append(Character.toUpperCase(origin.charAt(++i)));
                } else if (Character.isLetterOrDigit(chr)) {
                    if (sb.length() == 0) {
                        if (Character.isLetter(chr)) {
                            sb.append(chr);
                        }
                    } else {
                        sb.append(chr);
                    }
                }
            }
        } else {
            for (int i = 0; i < origin.length(); i++) {
                char chr = origin.charAt(i);
                if (Character.isLetterOrDigit(chr)) {
                    if (sb.length() == 0) {
                        if (Character.isLetter(chr)) {
                            sb.append(Character.toLowerCase(chr));
                        }
                    } else {
                        sb.append(chr);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串转换为其他类型
     * @param str 字符串
     * @param tClass 类型
     * @param <T> 类型
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    static <T> T ofString(String str, Class<T> tClass) {
        if (tClass == null | str == null) return null;
        else if (Boolean.class.equals(tClass) || boolean.class.equals(tClass)) {
            return (T) Boolean.valueOf(str);
        } else if (Character.class.equals(tClass) || char.class.equals(tClass)) {
            return (T) (str.isEmpty() ? null  : str.charAt(0));
        } else if (Byte.class.equals(tClass) || byte.class.equals(tClass)) {
            return (T) Byte.valueOf(Double.valueOf(str).byteValue());
        } else if (Short.class.equals(tClass) || short.class.equals(tClass)) {
            return (T) Short.valueOf(Double.valueOf(str).shortValue());
        } else if (Integer.class.equals(tClass) || int.class.equals(tClass)) {
            return (T) Integer.valueOf(Double.valueOf(str).intValue());
        } else if (Long.class.equals(tClass) || long.class.equals(tClass)) {
            return (T) Long.valueOf(Double.valueOf(str).longValue());
        } else if (Float.class.equals(tClass) || float.class.equals(tClass)) {
            return (T) Float.valueOf(Double.valueOf(str).floatValue());
        } else if (Double.class.equals(tClass) || double.class.equals(tClass)) {
            return (T) Double.valueOf(str);
        } else if (BigInteger.class.equals(tClass)) {
            return (T) new BigInteger(str);
        } else if (BigDecimal.class.equals(tClass)) {
            return (T) new BigDecimal(str);
        } else if (StringBuilder.class.equals(tClass)) {
            return (T) new StringBuilder(str);
        } else if (StringBuffer.class.equals(tClass)) {
            return (T) new StringBuffer(str);
        } else if (byte[].class.equals(tClass)) {
            return (T) str.getBytes(UTF8_CHARSET);
        } else {
            return null;
        }
    }
}
