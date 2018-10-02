package com.benzolamps.dict.advice;

import com.benzolamps.dict.dao.core.Pageable;
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

    @Around("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Class<?>[] paramTypes = signature.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (Pageable.class.equals(paramTypes[i]) && null == args[i]) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();
                String queryString = request.getQueryString();
                if (StringUtils.isEmpty(queryString)) {
                    args[i] = new Pageable();
                } else {
                    String json = URLDecoder.decode(queryString, "UTF-8");
                    args[i] = objectMapper.readValue(json, Pageable.class);
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
