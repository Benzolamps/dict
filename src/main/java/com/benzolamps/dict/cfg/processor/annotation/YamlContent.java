package com.benzolamps.dict.cfg.processor.annotation;

import com.benzolamps.dict.cfg.processor.FieldAnnotationResolver;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;

import static com.benzolamps.dict.util.Constant.YAML;
import static com.benzolamps.dict.util.DictSpring.$;

/**
 * 读取YAML中的内容并加载到实体类
 * @author Benzolamps
 * @version 2.3.9
 * @datetime 2019-1-7 16:59:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface YamlContent {

    /** Resource路径 */
    String value();
}

@Lazy(false)
@Component
class YamlContentResolver implements FieldAnnotationResolver<YamlContent> {
    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows(IOException.class)
    public <R> R resolve(YamlContent[] annotations, Object object, Field field) {
        YamlContent resourceContent = annotations[0];
        Resource resource = $(resourceContent.value(), Resource.class);
        return (R) YAML.loadAs(resource.getInputStream(), field.getType());
    }
}

