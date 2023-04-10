package com.mmacedoaraujo.userapi.aspect;

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

    //Defining pointcut for controller
    @Pointcut("execution(* com.mmacedoaraujo.userapi.controller.UserController..*(..))")
    protected void loggingOperationController() {

    }

    //Defining pointcut for service
    @Pointcut("execution(* com.mmacedoaraujo.userapi.service.serviceimpl.UserServiceImpl..*(..))")
    protected void loggingOperationService() {

    }

    //logging methods before they run at controller layer
    @Before("loggingOperationController()")
    @Order(1)
    public void logger(JoinPoint joinPoint) {
        log.info("Signature declaring type : " + joinPoint.getSignature().getDeclaringTypeName());
        log.info("Signature name : " + joinPoint.getSignature().getName());
        log.info("Arguments : " + Arrays.toString(joinPoint.getArgs()));
        log.info("Target class : " + joinPoint.getTarget().getClass().getName());
    }

    //logging return of methods at controller layer
    @AfterReturning(pointcut = "loggingOperationController()", returning = "result")
    @Order(2)
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Exiting from method: " + joinPoint.getSignature().getName());
        log.info("Return value: " + result);
    }

    //logging exceptions at service layer
    @AfterThrowing(value = "loggingOperationService()", throwing = "e")
    @Order(3)
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + "()");
        log.error("Cause " + e.getCause());
    }

    //logging methods at service layer
    @Around("loggingOperationService()")
    @Order(4)
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("The method " + joinPoint.getSignature().getName() + "() begins with " + Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("The method " + joinPoint.getSignature().getName() + "() ends with " + result);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }
}
