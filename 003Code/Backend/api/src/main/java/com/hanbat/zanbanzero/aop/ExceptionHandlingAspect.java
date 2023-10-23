package com.hanbat.zanbanzero.aop;

import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.exception.tool.SlackTools;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionHandlingAspect {

    private final SlackTools slackTools;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger("errorLogger");
    private final ThreadLocal<Boolean> authFlag = ThreadLocal.withInitial(() -> false);
    private final ThreadLocal<Boolean> serviceFlag = ThreadLocal.withInitial(() -> false);

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
    @AfterThrowing(pointcut = "authPointcut() || controllerPointcut()", throwing = "ex")
    public void handleAuthException(JoinPoint joinPoint, Exception ex) {
        if (serviceFlag.get() || authFlag.get()) {
            serviceFlag.remove();
            authFlag.remove();
        }
        else {
            authFlag.set(true);
            slackTools.sendSlackMessage(ex, joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), ex.getMessage());
        }
    }

    @AfterThrowing(pointcut = "servicePointcut()", throwing = "ex")
    public void handleServiceException(JoinPoint joinPoint, Exception ex) {
        serviceFlag.set(true);
        sendLog(joinPoint, ex);
        slackTools.sendSlackMessage(ex, joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), ex.getMessage());
    }

    private void sendLog(JoinPoint joinPoint, Exception ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String username = jwtUtil.getUsernameFromRequest(request);
        logger.error("[{}] {}:{} {} - {}",
                username,
                joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName(),
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }
}