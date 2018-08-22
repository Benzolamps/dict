package com.benzolamps.dict.cfg;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * Freemarker配置
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 20:19:07
 */
public class FreemarkerConfig {

    @Resource
    private Configuration configuration;

    /** global 变量 */
    @Setter
    private Map<String, Object> sharedVariables;

    @PostConstruct
    private void setConfigure() throws TemplateModelException {
        configuration.setSharedVaribles(sharedVariables);
        configuration.setAutoIncludes(Collections.singletonList("view/includes/main.ftl"));
    }
}
