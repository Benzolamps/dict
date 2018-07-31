package com.benzolamps.dict.cfg.freemarker;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
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
public class JsonDumper implements TemplateDirectiveModel {

    @Resource
    private ObjectMapper mapper;

    /* 因为ObjectMapper在writeValue完成后自动关闭Writer
     * 而关闭之后模板文件后续内容将不再继续写入
     * 所以写之前先把AUTO_CLOSE_TARGET属性设为false
     * 写完成后再把属性设为true
     * 这样后续内容仍可以写出
     * 不过会不会存在线程不安全的问题
     */
    // FIXME: 线程不安全
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws IOException, TemplateModelException {

        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        Assert.isTrue(params.containsKey("obj"), "obj未指定");
        Object obj = DeepUnwrap.unwrap((TemplateModel) params.get("obj"));
        Assert.notNull(obj, "obj不能为null");
        Writer out = env.getOut();
        mapper.writeValue(out, obj);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
    }
}
