package com.hanbat.zanbanzero.aop;

import org.aspectj.lang.JoinPoint;

public class Aspect {
    String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature()
                .getName();
    }
}
