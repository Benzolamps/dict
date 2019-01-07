package com.benzolamps.dict.advice;

import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.util.lambda.IntConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.stream.IntStream;

import static com.benzolamps.dict.util.Constant.UTF8_CHARSET;

/**
 * 分页建言
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-6 19:26:02
 */
@Aspect
@Component
public class PageableAdvice {

    @Resource
    private ObjectMapper objectMapper;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Class<?>[] paramTypes = signature.getParameterTypes();
        IntStream.range(0, paramTypes.length)
            .filter(i -> Pageable.class.equals(paramTypes[i]) && null == args[i]).findFirst()
            .ifPresent((IntConsumer) i -> {
                /* 获取url参数 */
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();
                String queryString = request.getQueryString();

                /* 根据参数生成Pageable对象 */
                if (StringUtils.hasText(queryString)) {
                    String json = URLDecoder.decode(queryString, UTF8_CHARSET.name());
                    args[i] = objectMapper.readValue(json, Pageable.class);
                } else {
                    args[i] = new Pageable();
                }
            });
        return joinPoint.proceed(args);
    }
}
