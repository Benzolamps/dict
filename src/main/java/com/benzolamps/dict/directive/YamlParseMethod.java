package com.benzolamps.dict.directive;

import com.benzolamps.dict.util.DictLambda;
import com.benzolamps.dict.util.ResourceType;
import freemarker.template.TemplateMethodModelEx;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.util.List;

/**
 * 将YAML资源转换为对象的Freemarker方法
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-11 18:40:54
 */
@Component
public class YamlParseMethod implements TemplateMethodModelEx {

    /** 资源类型 */
    @Setter
    private ResourceType resourceType;

    @Resource
    private Yaml yaml;

    public YamlParseMethod() {
        this.resourceType = ResourceType.STRING;
    }

    @Override
    public Object exec(List arguments) {
        Assert.notNull(resourceType, "resource type不能为空");
        Assert.notEmpty(arguments, "arguments不能为空");
        String str = arguments.get(0).toString();
        Assert.notNull(str, "str不能为null或空");
        return DictLambda.tryFunc(() -> execInternal(str));
    }

    private Object execInternal(String str) throws Exception {
        switch (resourceType) {
            case STRING:
                return yaml.load(str);
            case URL:
                return yaml.load(new UrlResource(str).getInputStream());
            case CLASS_PATH:
                return yaml.load(new ClassPathResource(str).getInputStream());
            case FILE_SYSTEM:
                return yaml.load(new FileSystemResource(str).getInputStream());
            default:
                return null;
        }
    }
}