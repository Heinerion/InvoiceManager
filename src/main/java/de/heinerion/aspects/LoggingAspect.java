package de.heinerion.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingAspect {
  private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

  @Around("execution(* *(..)) && @annotation(LogMethod)")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    String method = joinPoint.getSignature().getName();
    String className = joinPoint.getSignature().getDeclaringTypeName();
    logger.info(String.format("Start %s :: %s", className, method));

    Object result = joinPoint.proceed();

    logger.info(String.format("Exit  %s :: %s", className, method));

    return result;
  }
}
