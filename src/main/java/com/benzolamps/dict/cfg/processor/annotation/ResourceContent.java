package com.benzolamps.dict.cfg.processor.annotation;

import com.benzolamps.dict.cfg.processor.FieldAnnotationResolver;
import com.benzolamps.dict.util.DictObject;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;

import static com.benzolamps.dict.util.Constant.UTF8_CHARSET;
import static com.benzolamps.dict.util.DictSpring.$;

/**
 * 将Resource中读取内容
 * @author Benzolamps
 * @version 2.3.9
 * @datetime 2018-12-15 10:55:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface ResourceContent {

    /** Resource路径 */
    String value();
}

@Lazy(false)
@Component
class ResourceContentResolver implements FieldAnnotationResolver<ResourceContent> {
    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows(IOException.class)
    public <R> R resolve(ResourceContent[] annotations, Object object, Field field) {
        ResourceContent resourceContent = annotations[0];
        org.springframework.core.io.Resource resource = $(resourceContent.value(), Resource.class);
        if (field.getType().equals(byte[].class)) return (R) StreamUtils.copyToByteArray(resource.getInputStream());
        else return (R) DictObject.ofObject(StreamUtils.copyToString(resource.getInputStream(), UTF8_CHARSET), field.getType());
    }
}

