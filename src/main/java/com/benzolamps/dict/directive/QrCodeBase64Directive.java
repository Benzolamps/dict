package com.benzolamps.dict.directive;

import com.benzolamps.dict.util.DictObject;
import com.benzolamps.dict.util.DictQrCode;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * 文字转二维码Base64 Freemarker指令
 * @author 国文源
 * @version 2.2.3
 * @datetime 2018-11-2 15:45:21
 */
@Component
public class QrCodeBase64Directive implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Assert.isTrue(params.containsKey("content"), "content未指定");
        String content = Objects.toString(DeepUnwrap.unwrap((TemplateModel) params.get("content")));
        Assert.hasText(content, "content不能为null或空");
        Integer width = null, height = null;
        if (params.containsKey("width")) {
            width = DictObject.ofObject(DeepUnwrap.unwrap((TemplateModel) params.get("width")), int.class);
        }
        if (params.containsKey("height")) {
            height = DictObject.ofObject(DeepUnwrap.unwrap((TemplateModel) params.get("height")), int.class);
        }
        if (width == null) {
            width = 100;
        }
        if (height == null) {
            height = 100;
        }
        byte[] bytes = content.getBytes("UTF-8");
        Random random = new Random(2018);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= (random.nextInt(Byte.MAX_VALUE * 2) - Byte.MAX_VALUE);
        }
        content = Base64.getEncoder().encodeToString(bytes);
        String base64 = Base64.getEncoder().encodeToString(DictQrCode.createQrCode(content, width, height));
        env.getOut().write(base64);
    }
}
