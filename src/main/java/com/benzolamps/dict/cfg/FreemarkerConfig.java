package com.benzolamps.dict.cfg;

import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

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
@ImportResource(locations = "classpath:freemarker.xml")
@Configuration
public class FreemarkerConfig {

    @Resource
    private freemarker.template.Configuration configuration;

    /** global 变量 */
    @Setter
    private Map<String, Object> sharedVariables;

    @PostConstruct
    private void setConfigure() throws freemarker.template.TemplateModelException {
        configuration.setSharedVaribles(sharedVariables);
        configuration.setAutoIncludes(Collections.singletonList("view/includes/main.ftl"));
    }
}
