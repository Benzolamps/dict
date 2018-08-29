package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.component.DictOptions;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictRemote;
import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictList;
import com.benzolamps.dict.util.DictProperty;
import lombok.Getter;
import org.apache.commons.lang.enums.EnumUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.persistence.Id;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 自定义属性Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-22 23:11:50
 */
public class DictPropertyInfoVo implements Serializable {

    private static final long serialVersionUID = -1774782907582679818L;

    /* DictProperty */
    private final transient DictProperty dictProperty;

    /* DictPropertyInfo */
    private final transient DictPropertyInfo dictPropertyInfo;

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
    private final Collection<?> options;

    /** 最大值 */
    @Getter
    private final Object min;

    /** 最小值 */
    @Getter
    private final Object max;
    
    /** 最小长度 */
    @Getter
    private final Long minLength;

    /** 最大长度 */
    @Getter
    private final Long maxLength;

    /** 是否是过去的时间 */
    @Getter
    private boolean past;

    /** 是否是将来的时间 */
    @Getter
    private final boolean future;

    /** 正则表达式 */
    @Getter
    private final String pattern;

    /** 字符串是否可为空 */
    @Getter
    private final boolean notEmpty;

    /** 远程验证 */
    @Getter
    private final String remote;

    /** 是否是id */
    @Getter
    private final Boolean id;

    /**
     * 构造器
     * @param dictProperty 自定义属性
     */
    public DictPropertyInfoVo(DictProperty dictProperty) {
        Assert.notNull(dictProperty, "dict property不能为null");
        this.dictProperty = dictProperty;
        this.name = this.dictProperty.getName();
        this.dictPropertyInfo = this.dictProperty.getAnnotation(DictPropertyInfo.class);
        this.type = this.internalGetType();
        this.defaultValue = this.internalGetDefaultValue();
        this.min = this.internalGetMin();
        this.max = this.internalGetMax();
        this.minLength = this.internalGetMinLength();
        this.maxLength = this.internalGetMaxLength();
        this.options = this.internalGetOptions();
        this.past = this.internalIsPast();
        this.future = this.internalIsFuture();
        this.pattern = this.internalGetPattern();
        this.notEmpty = this.internalIsNotEmpty();
        this.remote = this.internalGetRemote();
        this.id = this.internalIsId();
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
            return null;
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

    private Object internalGetMax() {
        Long value = null;
        if (dictProperty.hasAnnotation(Max.class)) {
            value = dictProperty.getAnnotation(Max.class).value();
        } else if (dictProperty.hasAnnotation(Range.class)) {
            value = dictProperty.getAnnotation(Range.class).max();
        }

        if (value != null) {
            if ("char".equals(getType())) {
                return (char) (long) value;
            } else if ("integer".equals(getType()) || "float".equals(getType())) {
                return value;
            }
        }
        return value;
    }

    private Object internalGetMin() {
        Long value = null;
        if (dictProperty.hasAnnotation(Min.class)) {
            value = dictProperty.getAnnotation(Min.class).value();
        } else if (dictProperty.hasAnnotation(Range.class)) {
            value = dictProperty.getAnnotation(Range.class).min();
        }

        if (value != null) {
            if ("char".equals(getType())) {
                return (char) (long) value;
            } else if ("integer".equals(getType()) || "float".equals(getType())) {
                return value;
            }
        }
        return value;
    }
    
    private Long internalGetMinLength() {
        if (dictProperty.hasAnnotation(Length.class)) {
            return (long) dictProperty.getAnnotation(Length.class).min();
        }
        
        return null;
    }

    private Long internalGetMaxLength() {
        if (dictProperty.hasAnnotation(Length.class)) {
            return (long) dictProperty.getAnnotation(Length.class).max();
        }

        return null;
    }

    private List<?> internalGetOptions() {
        if (Arrays.asList("string", "integer", "float", "char", "date").contains(getType())) {
            if (dictProperty.hasAnnotation(DictOptions.class)) {
                Stream<String> values = Arrays.stream(dictProperty.getAnnotation(DictOptions.class).value());
                if ("integer".equals(getType())) {
                    return values.map(Long::valueOf).sorted().collect(Collectors.toList());
                } else if ("float".equals(getType())) {
                    return values.map(Double::valueOf).sorted().collect(Collectors.toList());
                } else {
                    return values.sorted().collect(Collectors.toList());
                }
            } else if ("integer".equals(getType()) || "char".equals(getType())) {
                if (getMax() != null && getMin() != null) {
                    if ("integer".equals(getType())) {
                        long max = (Long) getMax();
                        long min = (Long) getMin();
                        if (max - min > 0 && max - min <= 100) {
                            return DictList.range(min, max + 1);
                        }
                    } else if ("char".equals(getType())) {
                        char max = (Character) getMax();
                        char min = (Character) getMin();
                        if (max - min > 0 && max - min <= 100) {
                            return DictList.range(min, max + 1).stream().map(chr -> String.valueOf((char) (long) chr)).collect(Collectors.toList());
                        }
                    }
                }
            }
        } else if ("enum".equals(getType())) {
            List<?> enumList = EnumUtils.getEnumList(dictProperty.getType());
            return enumList.stream().map(Object::toString).collect(Collectors.toList());
        }
        return null;
    }

    private boolean internalIsPast() {
        return dictProperty.hasAnnotation(Past.class);
    }

    private boolean internalIsFuture() {
        return dictProperty.hasAnnotation(Future.class);
    }

    private boolean internalIsNotEmpty() {
        return dictProperty.hasAnnotation(NotEmpty.class);
    }

    private String internalGetPattern() {
        if (dictProperty.hasAnnotation(Pattern.class)) {
            return dictProperty.getAnnotation(Pattern.class).regexp();
        }
        return null;
    }

    private String internalGetRemote() {
        if (dictProperty.hasAnnotation(DictRemote.class)) {
            return dictProperty.getAnnotation(DictRemote.class).value();
        }
        return null;
    }

    private boolean internalIsId() {
        return dictProperty.hasAnnotation(Id.class);
    }

    /**
     * 根据类型生成一个自定义属性的集合
     * @param clazz 类型
     * @param <T> 类型
     * @return 自定义属性的集合
     */
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
