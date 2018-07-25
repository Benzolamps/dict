package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.util.DictBean;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 自定义属性Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-22 23:11:50
 */
public class DictPropertyInfoVo extends BaseVo {

    private static final long serialVersionUID = -1774782907582679818L;

    /** 名称 */
    @Getter
    private final String name;

    /* 自定义属性注解 */
    private final DictPropertyInfo dictPropertyInfo;

    /**
     * 构造器
     * @param name 名称
     * @param dictPropertyInfo 自定义属性注解
     */
    @SuppressWarnings("deprecation")
    public DictPropertyInfoVo(@NonNull String name, DictPropertyInfo dictPropertyInfo) {
        Assert.hasLength(name);
        this.name = name;
        this.dictPropertyInfo = dictPropertyInfo;
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

    /**
     * 根据类型生成一个自定义属性的集合
     * @param clazz 类型
     * @param <T> 类型
     * @return 自定义属性的集合
     */
    @SuppressWarnings("deprecation")
    public static <T> Collection<DictPropertyInfoVo> applyDictPropertyInfo(@NonNull Class<T> clazz) {
        DictBean<T> bean = DictBean.createBean(clazz, null);
        return bean.getProperties().stream()
            .map(property -> new DictPropertyInfoVo(property.getName(), (DictPropertyInfo) property.getAnnotation(DictPropertyInfo.class)))
            .collect(Collectors.toSet());
    }
}
