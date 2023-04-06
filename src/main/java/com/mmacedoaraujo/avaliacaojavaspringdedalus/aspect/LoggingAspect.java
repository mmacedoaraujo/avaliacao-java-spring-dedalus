package com.mmacedoaraujo.avaliacaojavaspringdedalus.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Pointcut("execution(* com.mmacedoaraujo.avaliacaojavaspringdedalus.controller.UserController..*(..))")
    protected void loggingOperationController() {

    }

    @Pointcut("execution(* com.mmacedoaraujo.avaliacaojavaspringdedalus.service.serviceimpl.UserServiceImpl..*(..))")
    protected void loggingOperationService() {

    }

    @Before("loggingOperationController()")
    @Order(1)
    public void logger(JoinPoint joinPoint) {
        log.info("Signature declaring type : " + joinPoint.getSignature().getDeclaringTypeName());
        log.info("Signature name : " + joinPoint.getSignature().getName());
        log.info("Arguments : " + Arrays.toString(joinPoint.getArgs()));
        log.info("Target class : " + joinPoint.getTarget().getClass().getName());
    }

    @AfterReturning(pointcut = "loggingOperationController()", returning = "result")
    @Order(2)
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Exiting from method: " + joinPoint.getSignature().getName());
        log.info("Return value: " + result);
    }

    @AfterThrowing(value = "loggingOperationService()", throwing = "e")
    @Order(3)
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + "()");
        log.error("Cause " + e.getCause());
    }
}