package hello.aop.order.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class AspectV2 {
    
    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))") //pointcut expression
    private void allOrder(){} // pointcut signature, 메서드 이름과 파라미터를 합쳐서 포인트컷 시그니처라 한다, 여러군데에서 쓸수 있음

    // 해당 표현식을 @Around에서 쓸수있다
    @Around("allOrder()") 
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); 
        return joinPoint.proceed();
    }
}
