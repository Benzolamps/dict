package com.benzolamps.dict.main;

import com.benzolamps.dict.util.DictMap;
import com.benzolamps.dict.util.DictSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * SpringBoot入口类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-11 19:10:18
 */
@Component
@SpringBootApplication
@ComponentScan(basePackages = "com.benzolamps.dict")
@EntityScan(basePackages="com.benzolamps.dict.bean")
@EnableJpaRepositories(basePackages= "com.benzolamps.dict.dao")
@ImportResource(locations = "classpath:freemarker.xml")
// @PropertySource(value = "file:dictionary.yml", factory = YamlPropertyLoaderFactory.class)
@EnableTransactionManagement
@EnableCaching
public interface DictApplication {
    /**
     * main方法
     * @param args 参数
     */
    static void main(String... args) {
        Yaml yaml = new Yaml();
        Map map = yaml.loadAs(tryFunc(new FileSystemResource("dictionary.yml")::getInputStream), Map.class);
        Properties properties = new Properties();
        properties.putAll(DictMap.convertToProperties(map));
        System.getProperties().forEach(properties::putIfAbsent);
        new SpringApplicationBuilder(DictApplication.class).properties(properties).initializers(applicationContext -> {
            DictSpring.setApplicationContext(applicationContext);
            DictSpring.setBean("classLoader", applicationContext.getClassLoader());
            DictSpring.setBean("yaml", yaml);
            List<String> profiles = Arrays.asList(applicationContext.getEnvironment().getActiveProfiles());
            DictSpring.setBean("isRelease", profiles.contains("release"));
        }).build().run(args);
    }
}
