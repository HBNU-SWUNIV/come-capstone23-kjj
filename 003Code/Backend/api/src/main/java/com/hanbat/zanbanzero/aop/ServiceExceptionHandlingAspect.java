package com.hanbat.zanbanzero.aop;

import com.hanbat.zanbanzero.exception.tool.SlackTools;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ServiceExceptionHandlingAspect {

    private final SlackTools slackTools;

    @AfterThrowing(pointcut = "execution(* com.hanbat.zanbanzero.service..*.*(..))", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) {
        slackTools.sendSlackMessage(ex, joinPoint.getSignature().getName(), ex.getMessage());
    }
}
