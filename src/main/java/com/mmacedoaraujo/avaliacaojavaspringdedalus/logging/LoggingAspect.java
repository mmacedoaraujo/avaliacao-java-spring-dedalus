package com.mmacedoaraujo.avaliacaojavaspringdedalus.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.mmacedoaraujo.avaliacaojavaspringdedalus.controller.UserController..*(..))")
    protected void loggingOperation() {

    }

    @Before("loggingOperation()")
    @Order(1)
    public void logger(JoinPoint joinPoint) {
        log.info("Signature declaring type : " + joinPoint.getSignature().getDeclaringTypeName());
        log.info("Signature name : " + joinPoint.getSignature().getName());
        log.info("Arguments : " + Arrays.toString(joinPoint.getArgs()));
        log.info("Target class : " + joinPoint.getTarget().getClass().getName());
    }

//    @AfterReturning(pointcut = "loggin")
}
