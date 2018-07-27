package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.component.DictOptions;
import com.benzolamps.dict.util.DictProperty;
import lombok.Getter;
import org.apache.commons.lang.enums.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 自定义属性Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-22 23:11:50
 */
public class DictPropertyInfoVo extends BaseVo {

    private static final long serialVersionUID = -1774782907582679818L;

    /* DictProperty */
    private final DictProperty dictProperty;

    /** 名称 */
    @Getter
    private final String name;

    /** 类型 */
    @Getter
    private final String type;

    /** 默认值 */
    @Getter
    private final Object defaultValue;

    /** 选项 */
    @Getter
    private final Collection<String> options;

    /* DictPropertyInfo */
    private final DictPropertyInfo dictPropertyInfo;

    /**
     * 构造器
     * @param dictProperty 自定义属性
     */
    public DictPropertyInfoVo(DictProperty dictProperty) {
        Assert.notNull(dictProperty, "dict property不能为null");
        this.dictProperty = dictProperty;
        this.name = dictProperty.getName();
        this.dictPropertyInfo = dictProperty.getAnnotation(DictPropertyInfo.class);
        this.type = internalGetType();
        this.defaultValue = internalGetDefaultValue();
        this.options = internalGetOptions();
    }

    /** @return 获取展示的名称 */
    public String getDisplay() {
        String display = dictPropertyInfo == null ? name : dictPropertyInfo.display();
        return "".equals(display) ? name : display;
    }

    /** @return 获取描述 */
    public String getDescription() {
        return dictPropertyInfo == null ? "" :  dictPropertyInfo.description();
    }

    private String internalGetType() {
        Class<?> clazz = dictProperty.getType();
        if (String.class.equals(clazz)) {
            return "string";
        } else if (Arrays.<Class<?>> asList(
            Byte.class, byte.class,
            Short.class, short.class,
            Integer.class, int.class,
            Long.class, long.class,
            BigInteger.class
        ).contains(clazz)) {
            return "integer";
        } else if (Arrays.<Class<?>> asList(
            Float.class, float.class,
            Double.class, double.class,
            Number.class,
            BigDecimal.class
        ).contains(clazz)) {
            return "float";
        } else if (Arrays.<Class<?>> asList(Boolean.class, boolean.class).contains(clazz)) {
            return "boolean";
        } else if (Arrays.<Class<?>> asList(Character.class, char.class).contains(clazz)) {
            return "char";
        } else if (Enum.class.isAssignableFrom(clazz)) {
            return "enum";
        } else if (Date.class.isAssignableFrom(clazz)) {
            return "date";
        } else {
            throw new UnsupportedOperationException("未实现的类型");
        }
    }

    private Object internalGetDefaultValue() {
        if (!dictProperty.hasAnnotation(Value.class)) {
            return null;
        }

        String value = dictProperty.getAnnotation(Value.class).value();
        if ("string".equals(type) || "char".equals(type) || "enum".equals(type)) {
            return value;
        } else if ("integer".equals(type)) {
            return Long.valueOf(value);
        } else if ("float".equals(type)) {
            return Double.valueOf(value);
        } else if ("boolean".equals(type)) {
            return Boolean.valueOf(value);
        } else if ("date".equals(type)) {
            return tryFunc(() -> new SimpleDateFormat().parse(value));
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> internalGetOptions() {
        if ("string".equals(getType())) {
            if (dictProperty.hasAnnotation(DictOptions.class)) {
                return Arrays.stream(dictProperty.getAnnotation(DictOptions.class).value()).collect(Collectors.toList());
            }
        } else if ("enum".equals(getType())) {
            List<?> enumList = EnumUtils.getEnumList(dictProperty.getType());
            return enumList.stream().map(Object::toString).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 根据类型生成一个自定义属性的集合
     * @param clazz 类型
     * @param <T> 类型
     * @return 自定义属性的集合
     */
    @SuppressWarnings("deprecation")
    public static <T> Collection<DictPropertyInfoVo> applyDictPropertyInfo(Class<T> clazz) {
        Assert.notNull(clazz, "clazz不能为null");
        DictBean<T> bean = new DictBean<>(clazz);
        Set<DictPropertyInfoVo> set = new LinkedHashSet<>();
        for (DictProperty property : bean.getProperties()) {
            DictPropertyInfoVo dictPropertyInfoVo = new DictPropertyInfoVo(property);
            set.add(dictPropertyInfoVo);
        }
        return set;
    }
}
