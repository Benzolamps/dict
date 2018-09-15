package com.benzolamps.dict.main;

import com.benzolamps.dict.util.DictSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.List;

/**
 * SpringBoot入口类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-11 19:10:18
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.benzolamps.dict")
@EntityScan(basePackages="com.benzolamps.dict.bean")
@EnableJpaRepositories(basePackages= "com.benzolamps.dict.dao")
@ImportResource(locations = "classpath:freemarker.xml")
// @PropertySource(value = "file:dictionary.yml", factory = YamlPropertyLoaderFactory.class)
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
public interface DictApplication {
    /**
     * main方法
     * @param args 参数
     */
    static void main(String... args) {
        new SpringApplicationBuilder(DictApplication.class).initializers(applicationContext -> {
            DictSpring.setApplicationContext(applicationContext);
            DictSpring.setBean("classLoader", applicationContext.getClassLoader());
            List<String> profiles = Arrays.asList(applicationContext.getEnvironment().getActiveProfiles());
            DictSpring.setBean("isRelease", profiles.contains("release"));
        }).build().run(args);
    }
}
