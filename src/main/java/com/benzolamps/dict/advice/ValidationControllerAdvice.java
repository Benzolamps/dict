package com.benzolamps.dict.advice;

import com.benzolamps.dict.util.Constant;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.Arrays;

/**
 * 校验建言
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-31 21:59:16
 */
@Aspect
@Component
public class ValidationControllerAdvice {

    @Resource
    private Validator validator;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    private native void pointcut();

    @Around("pointcut()")
    private Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        val parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            val annotations = parameterAnnotations[i];
            Validated validated = (Validated) Arrays.stream(annotations).filter(Validated.class::isInstance).findFirst().orElse(null);
            val constraintViolations = validator.validate(args[i], validated != null ? validated.value() : Constant.EMPTY_CLASS_ARRAY);
            if (!constraintViolations.isEmpty()) {
                val constraintViolation = constraintViolations.iterator().next();
                String message = constraintViolation.getMessage() + ": " + paramNames[i] + "." +  constraintViolation.getPropertyPath();
                Assert.isTrue(false, message);
            }
        }
        return joinPoint.proceed(args);
    }
}
