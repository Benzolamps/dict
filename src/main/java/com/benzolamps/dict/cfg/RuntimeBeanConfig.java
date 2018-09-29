package com.benzolamps.dict.cfg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.benzolamps.dict.util.DictSpring.spel;

/**
 * 运行时bean声明
 * @author Benzolamps
 * @version 2.1.3
 * @datetime 2018-9-17 15:58:10
 */
@Component
@Slf4j
public class RuntimeBeanConfig {

    @SuppressWarnings("unused")
    @EventListener(condition = "not @environment.acceptsProfiles('test')")
    public void applicationListener(ContextRefreshedEvent contextRefreshedEvent) throws IOException {
        logger.info(spel("#{'**${dict.system.title} - ${dict.system.version} - 启动成功！'}"));
        if (spel("#{'${os.name}'.startsWith('Windows')}")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:2018/dict/index.html");
        }
    }
}
