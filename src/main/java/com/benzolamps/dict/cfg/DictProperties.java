package com.benzolamps.dict.cfg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;

/**
 * 自定义配置项
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 14:56:52
 */
@SuppressWarnings("unused")
@Lazy(false)
@Configuration
@ConfigurationProperties(prefix = "dict.system")
@Getter
@Setter
public class DictProperties implements Serializable {

    private static final long serialVersionUID = 2388800158030381132L;

    /** 系统名称 */
    private String name;

    /** 系统标题 */
    private String title;

    /** 系统版本 */
    private String version;

    /** 远程文件路径 */
    private String remoteBaseUrl;

    /** Universe路径 */
    private String universePath;
}
