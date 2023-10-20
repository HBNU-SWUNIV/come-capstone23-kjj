package com.hanbat.zanbanzero.aop;

import com.hanbat.zanbanzero.exception.tool.SlackTools;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ServiceExceptionHandlingAspect {

    private final SlackTools slackTools;

    // 접근제어자(생략) / 반환타입(*) / 선언타입(com.hanbat.zanbanzero.auth..) 및 하위 모든 패키지 / 클래스명, 메소드이름(*.*) / 파라미터(..)
    @Pointcut("execution(* com.hanbat.zanbanzero.auth..*.*(..))")
    private void authPointcut() {}

    @Pointcut("execution(* com.hanbat.zanbanzero.controller..*.*(..))")
    private void controllerPointcut() {}

    @Pointcut("execution(* com.hanbat.zanbanzero.service..*.*(..))")
    private void servicePointcut() {}

    /**
     * 에러 발생 시 slack 메시지 알림 전송하는 AOP
     */
    @AfterThrowing(pointcut = "authPointcut()", throwing = "ex")
    public void handleAuthException(JoinPoint joinPoint, Exception ex) {
        slackTools.sendSlackMessage(ex, "[auth Filter] ", joinPoint.getSignature().getName(), ex.getMessage());
    }

    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "ex")
    public void handleControllerException(JoinPoint joinPoint, Exception ex)  {
        slackTools.sendSlackMessage(ex, "[Controller] ", joinPoint.getSignature().getName(), ex.getMessage());
    }

    @AfterThrowing(pointcut = "servicePointcut()", throwing = "ex")
    public void handleServiceException(JoinPoint joinPoint, Exception ex) {
        slackTools.sendSlackMessage(ex, "[Service] ", joinPoint.getSignature().getName(), ex.getMessage());
    }
}