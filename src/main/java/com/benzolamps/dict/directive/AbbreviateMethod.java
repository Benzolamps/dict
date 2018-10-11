package com.benzolamps.dict.directive;

import com.benzolamps.dict.util.DictObject;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字符串缩略Freemarker方法
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-28 20:24:29
 */
@Component
public class AbbreviateMethod implements TemplateMethodModelEx {
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Assert.notEmpty(arguments, "arguments不能为空");
        Assert.isTrue(arguments.size() >= 3, "arguments个数错误");
        String str = DictObject.ofObject(DeepUnwrap.unwrap(((TemplateModel) arguments.get(0))), String.class);
        Integer width = DictObject.ofObject(DeepUnwrap.unwrap(((TemplateModel) arguments.get(1))), int.class);
        String ellipsis = DictObject.ofObject(DeepUnwrap.unwrap(((TemplateModel) arguments.get(2))), String.class);
        if (StringUtils.isEmpty(str) || width == null) {
            return str;
        }

        StringBuilder sb = new StringBuilder();

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (width > 0) {
                if (c > 255) width -= 2;
                else --width;
                sb.append(c);
                if (width <= 0 && i < chars.length - 1) {
                    sb.append(ellipsis == null ? "" : ellipsis);
                }
            }
        }
        return sb.toString();
    }
}
