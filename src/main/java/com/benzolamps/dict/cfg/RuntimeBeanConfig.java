package com.benzolamps.dict.cfg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 运行时bean声明
 * @author Benzolamps
 * @version 2.1.3
 * @datetime 2018-9-17 15:58:10
 */
@Slf4j
@Component
public class RuntimeBeanConfig {

    @Profile("release")
    @Bean("isRelease")
    protected boolean isRelease() {
        return true;
    }

    @Profile("default")
    @Bean("isRelease")
    protected boolean isNotRelease() {
        return false;
    }
}
