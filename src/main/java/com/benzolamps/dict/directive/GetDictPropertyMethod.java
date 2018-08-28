package com.benzolamps.dict.directive;

import com.benzolamps.dict.controller.vo.DictPropertyInfoVo;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 将类名转换为DictPropertyVo的Freemarker方法
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-24 19:48:14
 */
@Component
public class GetDictPropertyMethod implements TemplateMethodModelEx {

    @Resource
    private ClassLoader classLoader;

    @Override
    public Object exec(List arguments) {
        Assert.notEmpty(arguments, "arguments不能为空");
        String className = arguments.get(0).toString();
        Assert.notNull(className, "class name不能为null或空");
        Class<?> clazz = tryFunc(() -> ClassUtils.forName(className, classLoader));
        return DictPropertyInfoVo.applyDictPropertyInfo(clazz);
    }
}
