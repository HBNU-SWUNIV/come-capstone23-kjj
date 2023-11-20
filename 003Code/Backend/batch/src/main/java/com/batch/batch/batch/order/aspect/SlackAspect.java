package com.batch.batch.batch.order.aspect;

import com.batch.batch.tools.SlackTools;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackAspect {

    private final SlackTools slackTools;

    @Pointcut("execution(* com.batch.batch.batch.order.task..*.*(..))")
    private void taskPointcut() {}

    @AfterThrowing(pointcut = "taskPointcut()", throwing = "ex")
    public void connectionHandlerAop(JoinPoint joinPoint, Exception ex) throws Exception {
        slackTools.sendSlackErrorMessage(ex, "[" + joinPoint.getTarget().getClass().getSimpleName() + "]" + " : "+ joinPoint.getSignature().getName());
        throw ex;
    }
}
