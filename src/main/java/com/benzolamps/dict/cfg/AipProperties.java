package com.benzolamps.dict.cfg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;

/**
 * AIP文字识别配置
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-11-6 16:03:55
 */
@Lazy(false)
@Configuration
@ConfigurationProperties(prefix = "dict.aip")
@Getter
@Setter
public class AipProperties implements Serializable {

    private static final long serialVersionUID = -3084505476442599683L;

    private String appId;

    private String apiKey;

    private String secretKey;
}
