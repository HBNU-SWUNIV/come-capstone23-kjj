package com.hanbat.zanbanzero.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

@Aspect
@Slf4j
public class CheckRunningTime extends com.hanbat.zanbanzero.aop.Aspect {

    @Pointcut("@annotation(com.hanbat.zanbanzero.aop.annotation.RunningTime)")
    private void checkRunningTime() {}

    // 기본 패키지의 모든 메소드를 대상으로 지정
    @Pointcut("execution(* com.hanbat.zanbanzero.service..*.*(..))")
    private void cut() {}

    @Around("cut() && checkRunningTime()")
    public void loggingRunningTime(ProceedingJoinPoint joinPoint) throws Throwable { // 대상을 실행까지 할 수 있는 joinPoint
        // 메소드 수행 전, 측정 시작
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 메소드 수행
        Object returningObj = joinPoint.proceed();

        // 메소드 수행 후, 측정 종료 및 로깅
        stopWatch.stop();
        log.info("{}의 총 수행 시간 => {} sec", getMethodName(joinPoint), stopWatch.getTotalTimeSeconds());
    }
}
