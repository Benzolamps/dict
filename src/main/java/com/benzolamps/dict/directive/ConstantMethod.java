package com.benzolamps.dict.directive;

import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictObject;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * Constant Freemarker方法
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-31 13:31:42
 */
@Component
public class ConstantMethod implements TemplateMethodModelEx {
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Assert.notEmpty(arguments, "arguments不能为空");
        String var = DictObject.ofObject(DeepUnwrap.unwrap(((TemplateModel) arguments.get(0))), String.class);
        Assert.hasLength(var, "var不能为null或空");
        return tryFunc(() -> Constant.class.getDeclaredField(var).get(null));
    }
}
