package hello.aop.order.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AspectV5Order {

    @Aspect
    @Order(2) // 순서를 지정하고 싶으면 Order을 사용해야 하는데 어드바이스 단위가 아니라 클래스 단위로 적용할 수 있다
    // 그래서 애스팩트를 별도의 클래스로 분리해야한다, 내부클래스를 만듦
    public static class LogAspect {
        @Around("hello.aop.order.aop.Pointcuts.allOrder()") 
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature()); 
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TxAspec {

        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    
            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
                return result;         
            } 
            catch (Exception e) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;
            } 
            finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
    
        }
    }
}
