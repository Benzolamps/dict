package com.benzolamps.dict.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 对象工具类
 * @author Benzolamps 
 * @version 2.1.1
 * @datetime 2018-8-22 14:57:43
 */
@SuppressWarnings("unchecked")
public interface DictObject {

    /**
     * 类型转换, 转换值
     * @param obj 对象
     * @param tClass 类型
     * @param <T> 类型
     * @return 转换后的对象
     */
    static <T> T ofObject(Object obj, Class<T> tClass) {
        if (obj == null || tClass == null) return null;
        else if (tClass.isInstance(obj)) return (T) obj;
        else if (String.class.equals(tClass) || CharSequence.class.equals(tClass)) return (T) obj.toString();
        else if (Number.class.isAssignableFrom(tClass) && Number.class.isInstance(obj)) {
            if (Byte.class.equals(tClass) || byte.class.equals(tClass)) return (T) (Byte) ((Number) obj).byteValue();
            else if (Short.class.equals(tClass) || short.class.equals(tClass)) return (T) (Short) ((Number) obj).shortValue();
            else if (Integer.class.equals(tClass) || int.class.equals(tClass)) return (T) (Integer) ((Number) obj).intValue();
            else if (Long.class.equals(tClass) || long.class.equals(tClass)) return (T) (Long) ((Number) obj).longValue();
            else if (Float.class.equals(tClass) || float.class.equals(tClass)) return (T) (Float) ((Number) obj).floatValue();
            else if (Double.class.equals(tClass) || double.class.equals(tClass)) return (T) (Double) ((Number) obj).doubleValue();
            else if (BigInteger.class.equals(tClass)) return (T) BigInteger.valueOf(((Number) obj).longValue());
            else if (BigDecimal.class.equals(tClass)) return (T) BigDecimal.valueOf(((Number) obj).doubleValue());
            else return null;
        } else if (boolean.class.equals(tClass) && Boolean.class.isInstance(obj)) {
            return (T) obj;
        } else if (char.class.equals(tClass) && Character.class.isInstance(obj)) {
            return (T) obj;
        } else {
            return DictString.ofString(obj.toString(), tClass);
        }
    }

    /**
     * 安全类强制转换
     * @param obj 对象
     * @param tClass 类型
     * @param <T> 类型
     * @return 转换后的对象
     */
    static <T> T safeCast(Object obj, Class<T> tClass) {
        return tClass == null || !tClass.isInstance(obj) ? null : (T) obj;
    }
}
