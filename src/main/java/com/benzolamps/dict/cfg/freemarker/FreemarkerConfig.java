
package com.benzolamps.dict.cfg.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    @NonNull
    private Map<String, Object> sharedVariables;

    @PostConstruct
    private void setConfigure() throws TemplateModelException {
        configuration.setSharedVaribles(sharedVariables);
    }
}
