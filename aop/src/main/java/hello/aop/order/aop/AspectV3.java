package hello.aop.order.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class AspectV3 {
    
    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))") //pointcut expression
    private void allOrder(){} // pointcut signature

    // 클래스 이름 패턴이 *Service
    // 타입 패턴 이름이라고 한 이유는 클래스, 인터페이스에 모두 적용되기 떄문
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService(){}

    @Around("allOrder()") 
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); 
        return joinPoint.proceed();
    }

    // hello.aop.order 패키지와 하위 패키지 이면서 클래스 이름 패턴이 *Service
    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed(); // 타겟 호출
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
    /*
     * 포인트컷이 적용된 AOP 결과
     * orderService : doLog(), doTransaction() 어드바이스 적용
     * orderRepository : doLog 어드바이스 적용
     * 
     * AOP 적용 전
     * 클라이언트 orderService.orderItem() orderRepository.save()
     * AOP 적용 후
     * 클라이언트 [ doLog() doTransaction() ] orderService.orderItem()
     * [ doLog() ] orderRepository.save()
     */
}
