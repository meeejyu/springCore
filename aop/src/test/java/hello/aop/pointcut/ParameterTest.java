package hello.aop.pointcut;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;

/*
 * 매개 변수 전달
 * 포인트컷의 이름과 매개변수의 이름을 맞추어야 한다. 여기서는 arg 로 맞추었다.
 * 추가로 타입이 메서드에 지정한 타입으로 제한된다. 여기서는 메서드의 타입이 String 으로 되어 있기 때문에 다음과 같이 정의되는 것으로 이해하면 된다.
    args(arg,..) args(String,..)
 */
@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {
    
    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0]; // 파라미터값 꺼내기
            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1);

            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);

            return joinPoint.proceed();
        }

        @Before("allMember() && this(obj)") // 프록시를 전달받음
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this]{}. obj={}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)") // 구현체impl를 전달받음, 실제 대상을 전달받음, 프록시가 실제 호출한 타켓을 대상으로함
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target]{}. obj={}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && @target(annotation)")// 애노테이션 정보를 가져옴
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target]{}. obj={}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)") // 애노테이션 정보를 가져옴
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within]{}. obj={}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @annotation(annotation)") // 애노테이션에 들어있는 value를 값을 가져올수 있다
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation]{}. annotationValue={}", joinPoint.getSignature(), annotation.value());
        }

    }
}
