package hello.proxy.jdkdynamic;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/*
 * 자바 리플렉션
JDK동적 프록시를 이해하기 이해서는 먼저 자바의 리플렉션 기술을 이해해야 한다.
리플렉션 기술을 사용하면 클래스나 메서드의 메타정보를 동적으로 획득하고, 코드도 동적으로 호출할 수 있다.
리플렉션은 런타임 시점에 에러가 발생한다. 컴파일 시점에 오류를 잡을 수 없다
주의해서 사용해야 한다
 */
@Slf4j
public class ReflectionTest {
    
    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드가 다름
        log.info("result={}", result1);
        // 공통 로직1 종료 

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callA(); // 호출하는 메서드가 다름
        log.info("result={}", result2); 
        // 공통 로직2 종료 
        
    }

    @Test
    void reflection1() throws Exception{

        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        // 클래스 정보
        Hello target = new Hello();

        // callA 메소드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target); // 메소드를 호출한다, 해당 클래스안에 정보가 없다면 예외가 발생한다
        log.info("result1={}", result1);
        
        // callB 메소드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallA.invoke(target); 
        log.info("result2={}", result2);
    }

    @Test
    void reflection2() throws Exception{

        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        // 클래스 정보
        Hello target = new Hello();

        // callA 메소드 정보
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);
        
        // callA 메소드 정보
        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }
        public String callB() {
            log.info("callB");
            return "B";
        }
    }

}
