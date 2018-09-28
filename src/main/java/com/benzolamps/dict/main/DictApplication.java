package com.benzolamps.dict.main;

import com.benzolamps.dict.util.DictSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBoot入口类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-11 19:10:18
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.benzolamps.dict.dao.impl",
    "com.benzolamps.dict.service.impl",
    "com.benzolamps.dict.controller",
    "com.benzolamps.dict.cfg",
    "com.benzolamps.dict.advice",
    "com.benzolamps.dict.directive"
})
@EntityScan(basePackages = "com.benzolamps.dict.bean")
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
public class DictApplication {
    /**
     * main方法
     * @param args 参数
     */
    @SuppressWarnings("ConstantConditions")
    public static void main(String... args) {
        new SpringApplicationBuilder(DictApplication.class).initializers(DictSpring::setApplicationContext).build().run(args);
    }
}
