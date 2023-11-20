package com.hanbat.zanbanzero.aop;

import com.hanbat.zanbanzero.exception.tool.SlackTools;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class IgnoreCacheErrorAspect {

    private final SlackTools slackTools;
    private final RedisConnectionFactory redisConnectionFactory;

    @Around("@annotation(com.hanbat.zanbanzero.aop.annotation.IgnoreCacheErrors)")
    public Object ignoreCacheError(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            redisConnectionFactory.getConnection().close();
        } catch (RedisConnectionFailureException e) {
            slackTools.sendSlackMessage(e, "Redis connection fail. Check Redis Server");
        }
        return joinPoint.proceed();
    }
}
