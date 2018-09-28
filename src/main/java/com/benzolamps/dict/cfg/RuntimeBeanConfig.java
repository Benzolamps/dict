package com.benzolamps.dict.cfg;

import com.benzolamps.dict.util.DictSpring;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 运行时bean声明
 * @author Benzolamps
 * @version 2.1.3
 * @datetime 2018-9-17 15:58:10
 */
@Component
@Slf4j
public class RuntimeBeanConfig {

    @Bean("isRelease")
    protected Boolean isRelease() {
        return DictSpring.spel("#{environment.acceptsProfiles('release')}");
    }

    @SuppressWarnings("unused")
    @EventListener(condition = "not @environment.acceptsProfiles('test')")
    public void applicationListener(ContextRefreshedEvent contextRefreshedEvent) throws IOException {
        logger.info(DictSpring.spel("#{'${dict.system.title} - ${dict.system.version} - 启动成功！'}"));
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:2018/dict/index.html");
        }
    }
}
