package com.benzolamps.dict.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import static com.benzolamps.dict.util.Constant.EMPTY_CLASS_ARRAY;

/**
 * 校验建言
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-31 21:59:16
 */
@SuppressWarnings("ConstantConditions")
@Aspect
@Component
public class ValidationControllerAdvice {

    @Resource
    private Validator validator;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) continue;
            Annotation[] annotations = parameterAnnotations[i];
            Validated validated = (Validated) Arrays.stream(annotations).filter(Validated.class::isInstance).findFirst().orElse(null);
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(args[i], validated != null ? validated.value() : EMPTY_CLASS_ARRAY);
            if (!constraintViolations.isEmpty()) {
                ConstraintViolation<Object> constraintViolation = constraintViolations.iterator().next();
                String message = constraintViolation.getMessage() + ": " + paramNames[i] + "." +  constraintViolation.getPropertyPath();
                Assert.isTrue(false, message);
            }
        }
        return joinPoint.proceed(args);
    }
}
