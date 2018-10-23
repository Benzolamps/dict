package com.benzolamps.dict.directive;

import com.benzolamps.dict.controller.vo.DictPropertyInfoVo;
import com.benzolamps.dict.util.DictSpring;
import freemarker.template.TemplateMethodModelEx;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * 将类名转换为DictPropertyVo的Freemarker方法
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-24 19:48:14
 */
@Component
public class GetDictPropertyMethod implements TemplateMethodModelEx {

    @Override
    @SneakyThrows(ClassNotFoundException.class)
    public Object exec(@SuppressWarnings("rawtypes") List arguments) {
        Assert.notEmpty(arguments, "arguments不能为空");
        String className = arguments.get(0).toString();
        Assert.hasText(className, "class name不能为null或空");
        Class<?> clazz = ClassUtils.forName(className, DictSpring.getClassLoader());
        return DictPropertyInfoVo.applyDictPropertyInfo(clazz);
    }
}
