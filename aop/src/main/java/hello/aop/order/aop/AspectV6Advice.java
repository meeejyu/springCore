package hello.aop.order.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import lombok.extern.slf4j.Slf4j;

/*
 * 어드바이스 종류
    @Around : 메서드 호출 전후에 수행, 가장 강력한 어드바이스, 조인 포인트 실행 여부 선택, 반환 값 변환, 예외 변환 등이 가능
    @Before : 조인 포인트 실행 이전에 실행
    @AfterReturning : 조인 포인트가 정상 완료후 실행 
    @AfterThrowing : 메서드가 예외를 던지는 경우 실행
    @After : 조인 포인트가 정상 또는 예외에 관계없이 실행(finally)

    @Around
    메서드의 실행의 주변에서 실행된다. 메서드 실행 전후에 작업을 수행한다. 
    가장 강력한 어드바이스
        조인 포인트 실행 여부 선택 joinPoint.proceed() 호출 여부 선택 
        전달 값 변환: joinPoint.proceed(args[])
        반환 값 변환
        예외 변환
        트랜잭션 처럼 try ~ catch~ finally 모두 들어가는 구문 처리 가능 
    어드바이스의 첫 번째 파라미터는 ProceedingJoinPoint 를 사용해야 한다.
    proceed() 를 통해 대상을 실행한다. proceed() 를 여러번 실행할 수도 있음(재시도)
 */
@Slf4j
@Aspect
public class AspectV6Advice {

    // 실행을 시키기 위해서 ProceedingJoinPoint 가 반드시 있어야햄. 
    // 참고로 ProceedingJoinPoint 는 org.aspectj.lang.JoinPoint 의 하위 타입이다.
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            // @Before
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            // @AfterReturning
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;         
        } 
        catch (Exception e) {
            // @AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } 
        finally {
            // @After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }

    }

    /*
     * 실행 순서
     * 어드바이스가 적용되는 순서는 이렇게 적용되지만, 호출 순서와 리턴 순서는 반대
     * 어드바이스 적용 순서
     *  @Around, @Before, @After, @AfterReturning, @AfterThrowing
     * 리턴 순서
     *  @Around, @Before, @AfterThrowing, @AfterReturning, @After 
       물론 @Aspect 안에 동일한 종류의 어드바이스가 2개 있으면 순서가 보장되지 않는다. 
       이 경우 앞서 배운 것 처럼 @Aspect 를 분리하고 @Order 를 적용하자.
     */

    // 비포를 알아서 실행해줌. joinPoint가 실행되기 전에만 개발
    // 작업에 흐름을 변경할 수 없다.
    // 메서드 종료시 자동으로 다음 타겟이 호출된다
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    // 리턴값을 조작할 수 없다, 반환되는 객체를 변경할 수 없다
    // returning = "result" 과 Object result 대조되서 리턴값을 가져올 수 있다, 이름이 일치해야됨
    // 타입이 안맞으면 아예 호출이 안됨
    @AfterReturning(value= "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value= "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }

    /*
     * @Around 외에 다른 어드바이스가 존재하는 이유
     * @Around 가 가장 넓은 기능을 제공하는 것은 맞지만, 실수할 가능성이 있다. 
     * 반면에 @Before , @After 같은 어드바이스는 기능은 적지만 실수할 가능성이 낮고, 코드도 단순하다. 
     * 그리고 가장 중요한 점이 있는데, 바로 이 코드를 작성한 의도가 명확하게 들어난다는 점이다
     */
}
