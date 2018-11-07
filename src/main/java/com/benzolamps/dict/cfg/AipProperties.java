package com.benzolamps.dict.cfg;

import com.baidu.aip.ocr.AipOcr;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;

/**
 * AIP文字识别配置
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-11-6 16:03:55
 */
@Lazy(false)
@Configuration
@ConfigurationProperties(prefix = "dict.aip")
public class AipProperties implements Serializable {

    private static final long serialVersionUID = -3084505476442599683L;

    @Setter
    private String appId;

    @Setter
    private String apiKey;

    @Setter
    private String secretKey;

    private transient AipOcr client;

    @PostConstruct
    private void postConstruct() {
        client = new AipOcr(appId, apiKey, secretKey);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    public JSONObject accurateGeneral(byte[] data) {
        return client.accurateGeneral(data, new HashMap<>());
    }
}
