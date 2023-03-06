package com.hanbat.zanbanzero.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ServiceLoggingAspect extends com.hanbat.zanbanzero.aop.Aspect {

    public String template = "Method [{}], input [{}]";

    @Pointcut("@annotation(com.hanbat.zanbanzero.aop.annotation.ServiceLogging)")
    private void serviceLogging() {}

    @Pointcut("execution(* com.hanbat.zanbanzero.service..*.*(..))")
    private void cut() {}

    @AfterReturning("cut() && serviceLogging()")
    public void logging(JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        Object[] args = joinPoint.getArgs();
        log.info(template, methodName, Arrays.stream(args).toList());
    }
}
