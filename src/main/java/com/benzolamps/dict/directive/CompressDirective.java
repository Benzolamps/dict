package com.benzolamps.dict.directive;

import com.benzolamps.dict.util.DictSpring;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * 压缩代码Freemarker指令
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-10-1 11:12:45
 */
@Component
public class CompressDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        StringWriter stringWriter = new StringWriter();
        body.render(stringWriter);
        String str = DictSpring.<UnaryOperator<String>> getBean("compress").apply(stringWriter.toString());
        env.getOut().write(str);
    }
}
