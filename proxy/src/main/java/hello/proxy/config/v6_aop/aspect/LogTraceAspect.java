package hello.proxy.config.v6_aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect // 애노테이션 기반 프록시를 적용할 때 필요
public class LogTraceAspect {
    
    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    /*
     *  @Around("execution(* hello.proxy.app..*(..))")
        public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        @Around의 값에 포인트 컷 표현식을 넣는다, 표현식이 포인트컷
        @Around의 메서드는 어드바이스가 된다
     */
    //  
    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = null;

        log.info("target={}", joinPoint.getTarget()); // 실제 호출 대상
        log.info("getArgs={}", joinPoint.getTarget()); // 전달 인자
        log.info("getSignature={}", joinPoint.getTarget()); // join point 시그니처

        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);   

            // 로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } 
        catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    
}
