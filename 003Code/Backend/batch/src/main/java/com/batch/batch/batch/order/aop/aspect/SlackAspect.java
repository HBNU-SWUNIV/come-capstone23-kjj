package com.batch.batch.batch.order.aop.aspect;

import com.batch.batch.tools.SlackTools;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackAspect {

    private final SlackTools slackTools;
    private final DataSource dataSource;

    @Pointcut("execution(* com.batch.batch.batch.order.task..*.*(..))")
    private void taskPointcut() {}

    @Around("taskPointcut()")
    public Object connectionHandlerAop(ProceedingJoinPoint joinPoint) throws Throwable {
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            Object proceed = joinPoint.proceed();
            connection.commit();

            return proceed;
        } catch (Exception e) {
            connection.rollback();
            slackTools.sendSlackErrorMessage(e, "[" + joinPoint.getTarget().getClass().getSimpleName() + "]" + " : "+ joinPoint.getSignature().getName());
            throw e;
        } finally {
            connection.close();
        }
    }
}
