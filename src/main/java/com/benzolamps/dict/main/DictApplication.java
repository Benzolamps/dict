package com.benzolamps.dict.main;

import com.benzolamps.dict.util.DictMap;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.yaml.snakeyaml.Yaml;

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
public abstract class DictApplication {

    private static Properties properties;

    static {
        Yaml yaml = new Yaml();
        Map map = yaml.loadAs(tryFunc(new FileSystemResource("dictionary.yml")::getInputStream), Map.class);
        properties = new Properties();
        properties.putAll(DictMap.convertToProperties(map));
        System.getProperties().forEach(properties::putIfAbsent);
    }

    /** @return isRelease */
    @Bean("isRelease")
    @Profile("release")
    protected boolean isRelease() {
        return true;
    }

    /** @return isRelease */
    @Bean("isRelease")
    @Profile("default")
    protected boolean isNotRelease() {
        return false;
    }

    /**
     * main方法
     * @param args 参数
     */
    public static void main(String... args) {
        new SpringApplicationBuilder(DictApplication.class).properties(properties).build().run(args);
    }
}
