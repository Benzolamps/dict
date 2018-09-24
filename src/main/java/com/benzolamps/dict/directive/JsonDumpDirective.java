package com.benzolamps.dict.directive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * 将实体类转换为JSON串的Freemarker指令
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 21:23:40
 */
@Component
public class JsonDumpDirective implements TemplateDirectiveModel {

    @Resource
    private ObjectMapper mapper;

    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws IOException, TemplateException {
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        Assert.isTrue(params.containsKey("obj"), "obj未指定");
        Object obj = DeepUnwrap.unwrap((TemplateModel) params.get("obj"));
        Assert.notNull(obj, "obj不能为null");
        Writer out = env.getOut();
        mapper.writeValue(out, obj);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
    }
}
