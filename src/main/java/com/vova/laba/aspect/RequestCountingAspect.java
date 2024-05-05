package com.vova.laba.aspect;

import com.vova.laba.service.RequestCounterService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RequestCountingAspect {
  RequestCounterService requestCounterService;

  @Autowired
  RequestCountingAspect(RequestCounterService requestCounterService) {
    this.requestCounterService = requestCounterService;
  }

  @Around(
      "@within(com.vova.laba.aspect.annotation.RequestCounting) ||"
          + "@annotation(com.vova.laba.aspect.annotation.RequestCounting)")
  public Object incrementRequestCounter(ProceedingJoinPoint joinPoint) throws Throwable {
    requestCounterService.increment();
    return joinPoint.proceed();
  }
}
