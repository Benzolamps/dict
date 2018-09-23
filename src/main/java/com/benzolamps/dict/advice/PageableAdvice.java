package com.benzolamps.dict.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.benzolamps.dict.dao.core.Pageable;

/**
 * 分页建言
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-6 19:26:02
 */
@Aspect
@Component
public class PageableAdvice {

    @Around("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Class<?>[] paramTypes = signature.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (Pageable.class.equals(paramTypes[i]) && null == args[i]) {
                args[i] = new Pageable();
            }
        }
        return joinPoint.proceed(args);
    }
}
