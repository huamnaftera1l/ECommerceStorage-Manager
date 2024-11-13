package com.jd.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.yourpackage.controller.*.*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 记录输入参数
        Object[] args = joinPoint.getArgs();
        logger.info("Entering {}::{} with arguments: {}", className, methodName, Arrays.toString(args));

        // 执行方法
        Object result = joinPoint.proceed();

        // 记录输出结果
        logger.info("Exiting {}::{} with result: {}", className, methodName, result);

        return result;
    }
}