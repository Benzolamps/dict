package com.benzolamps.dict.directive;

import com.benzolamps.dict.util.Constant;
import freemarker.core.Environment;
import freemarker.template.*;
import freemarker.template.utility.DeepUnwrap;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据修改的Freemarker指令
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-27 21:34:15
 */
@Component
public class DataEditDirective implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object id = DeepUnwrap.unwrap((TemplateModel) params.get("id"));
        Assert.notNull(id, "id不能为null");
        Object fields = DeepUnwrap.unwrap((TemplateModel) params.get("fields"));
        Assert.notNull(fields, "fields不能为null");
        Object values = DeepUnwrap.unwrap((TemplateModel) params.get("values"));
        if (values == null) values = Constant.EMPTY_MAP;
        Object rules = DeepUnwrap.unwrap((TemplateModel) params.get("rules"));
        if (rules == null) rules = Constant.EMPTY_MAP;
        Object messages = DeepUnwrap.unwrap((TemplateModel) params.get("messages"));
        if (messages == null) messages = Constant.EMPTY_MAP;
        Object title = DeepUnwrap.unwrap((TemplateModel) params.get("title"));
        if (title == null) title = "修改" + id;
        Object submitHandler = DeepUnwrap.unwrap((TemplateModel) params.get("submit_handler"));
        //language=JavaScript
        if (submitHandler == null) submitHandler = "return false;";
        Object readyHandler = DeepUnwrap.unwrap((TemplateModel) params.get("ready_handler"));
        //language=JavaScript
        if (readyHandler == null) readyHandler = "";
        Object update = DeepUnwrap.unwrap((TemplateModel) params.get("save"));
        if (update == null) update = "update.json";
        Template template = env.getConfiguration().getTemplate("/view/includes/edit.ftl");
        template.setAutoFlush(false);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("id", id);
        dataModel.put("fields", fields);
        dataModel.put("values", values);
        dataModel.put("rules", rules);
        dataModel.put("messages", messages);
        dataModel.put("title", title);
        dataModel.put("submit_handler", submitHandler);
        dataModel.put("ready_handler", readyHandler);
        dataModel.put("update", update);
        template.process(dataModel, env.getOut());
        template.setAutoFlush(true);
    }
}
