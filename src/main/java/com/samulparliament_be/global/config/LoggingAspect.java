package com.samulparliament_be.global.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // 모든 Controller 메서드에 자동 적용
    @Around("execution(* com.samulparliament_be..controller..*(..))")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        // 요청 파라미터 (민감정보 필터링)
        String args = formatArgs(joinPoint.getArgs());
        
        log.info("[API] {}.{} 호출 | args={}", className, methodName, args);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("[API] {}.{} 완료 | {}ms", className, methodName, duration);
            
            return result;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.warn("[API] {}.{} 실패 | {}ms | error={}", 
                    className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";
                    String str = arg.toString();
                    // 너무 긴 인자는 잘라서 표시
                    if (str.length() > 200) {
                        return str.substring(0, 200) + "...";
                    }
                    return str;
                })
                .toList()
                .toString();
    }
}
