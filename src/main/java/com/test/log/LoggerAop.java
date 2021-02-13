package com.test.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class LoggerAop {

    @Around("execution(* com.test..*(..))")
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        try {
            log.info("Started method [{}] execution from class [{}] ", methodName, className);
            Object result = joinPoint.proceed();
            return result;
        } finally {
            stopWatch.stop();
            log.info("Finished method [{}] execution from class [{}]. Time take in sec [{}]", methodName, className, stopWatch.getTotalTimeSeconds());
        }
    }
}
