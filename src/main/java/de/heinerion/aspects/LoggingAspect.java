package de.heinerion.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {
  @Around("execution(* *(..)) && @annotation(de.heinerion.aspects.annotations.LogMethod)")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    Logger logger = getLogger(joinPoint);

    String method = joinPoint.getSignature().getName();
    logger.info(String.format("Enter %s", method));

    Object result = joinPoint.proceed();

    logger.info(String.format("Exit  %s", method));

    return result;
  }

  private static Logger getLogger(JoinPoint joinPoint) {
    return LogManager.getLogger(joinPoint.getSignature().getDeclaringType());
  }

  @Before("execution(* *(..)) && @annotation(de.heinerion.aspects.annotations.LogBefore)")
  public void before(JoinPoint joinPoint) throws Throwable {
    Logger logger = getLogger(joinPoint);

    String method = joinPoint.getSignature().getName();
    logger.info(String.format("Enter %s", method));
  }
}
