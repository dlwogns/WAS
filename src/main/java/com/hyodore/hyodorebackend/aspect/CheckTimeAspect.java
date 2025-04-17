package com.hyodore.hyodorebackend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CheckTimeAspect {
  @Around("@annotation(com.hyodore.hyodorebackend.annotation.CheckExecutionTime)")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    Object result = joinPoint.proceed(); // 실제 메서드 실행

    long end = System.currentTimeMillis();
    log.info(joinPoint.getSignature() + " 실행 시간: " + (end - start) + "ms");

    return result;
  }
}
