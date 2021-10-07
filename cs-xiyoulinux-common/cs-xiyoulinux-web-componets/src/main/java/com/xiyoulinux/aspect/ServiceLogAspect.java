package com.xiyoulinux.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author CoderZk
 */
@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger log = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* com.xiyoulinux..*.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("======= 开始执行 {}.{} =======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        long begin = System.currentTimeMillis();

        // 执行目标 service
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        long takeTime = end - begin;

        if (takeTime > 3000) {
            log.error("====== 执行结束, 耗时: {} 毫秒 ======", takeTime);
        } else if (takeTime > 2000) {
            log.warn("====== 执行结束, 耗时: {} 毫秒 ======", takeTime);
        } else {
            log.info("====== 执行结束, 耗时: {} 毫秒 ======", takeTime);
        }

        return result;
    }
}
