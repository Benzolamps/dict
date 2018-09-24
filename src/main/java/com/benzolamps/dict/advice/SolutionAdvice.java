package com.benzolamps.dict.advice;

import com.benzolamps.dict.dao.base.DocSolutionDao;
import com.benzolamps.dict.dao.base.ShuffleSolutionDao;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 乱序方案建言
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-22 23:06:27
 */
@Component
@Aspect
public class SolutionAdvice {

    @Resource
    private ShuffleSolutionDao shuffleSolutionDao;

    @Resource
    private DocSolutionDao docSolutionDao;

    @Pointcut("execution(* com.benzolamps.dict.service.impl.*SolutionServiceImpl.*(..))")
    private native void pointcut();

    @Before("pointcut()")
    private void before() {
        shuffleSolutionDao.reload();
        docSolutionDao.reload();
    }

    @AfterReturning("pointcut()")
    private void afterReturning(JoinPoint point) {
        Transactional transactional = getTransactional(point);
        if (transactional == null || !transactional.readOnly()) {
            shuffleSolutionDao.flush();
            docSolutionDao.flush();
        }
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwable")
    private void afterThrowing(JoinPoint point, Throwable throwable) {
        Transactional transactional = getTransactional(point);
        if (transactional != null) {
            if (!ObjectUtils.isCheckedException(throwable)) {
                shuffleSolutionDao.reload();
                docSolutionDao.reload();
            }
        }
    }

    private Transactional getTransactional(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        val clazz = Transactional.class;
        if (method.isAnnotationPresent(clazz)) {
            return method.getDeclaredAnnotation(clazz);
        }
        return null;
    }
}
