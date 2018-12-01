package com.benzolamps.dict.cfg;

import com.benzolamps.dict.util.lambda.Action2;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.benzolamps.dict.util.DictSpring.getBean;
import static com.benzolamps.dict.util.DictSpring.resolve;

/**
 * 运行时bean声明
 * @author Benzolamps
 * @version 2.1.3
 * @datetime 2018-9-17 15:58:10
 */
@Component
@Slf4j
@ImportResource("classpath:freemarker.xml")
public class RuntimeBeanConfig {
    @Resource
    private Configuration configuration;

    @Lazy
    @Resource
    private Map freemarkerGlobals;

    @SuppressWarnings({"unused", "unchecked", "rawtypes", "ResultOfMethodCallIgnored"})
    @EventListener(condition = "not @environment.acceptsProfiles('test')")
    public void applicationListener(ContextRefreshedEvent contextRefreshedEvent) throws Exception {

        /* 加载Freemarker共享变量 */
        freemarkerGlobals.forEach((Action2<String, Object>) configuration::setSharedVariable);

        logger.info(resolve("#{'**${dict.system.title} - ${dict.system.version} - 启动成功！'}"));
        if (resolve("#{'${os.name}'.startsWith('Windows')}")) {
            Runtime.getRuntime().exec(resolve("#{'rundll32 url.dll,FileProtocolHandler http://localhost:${server.port}${server.context_path}/index.html'}").toString());
        }

        File file = new File("C:\\upload");
        if (file.isFile()) {
            file.delete();
        }
        file.mkdirs();

    }

    @Bean("compress")
    protected UnaryOperator<String> compress() {

        boolean isRelease = getBean(Environment.class).acceptsProfiles("release");

        return str -> {
            if (!StringUtils.hasText(str)) {
                return "";
            } else if (isRelease) {
                return str.replaceAll("[\\s\\u00a0]+", " ").replaceAll("> <", "><").trim();
            } else {
                return str.replaceAll("[\\t\\f\\u00a0 ]+", " ").replaceAll("[\\r\\n]+ ", "\r\n").trim();
            }
        };
    }
}
