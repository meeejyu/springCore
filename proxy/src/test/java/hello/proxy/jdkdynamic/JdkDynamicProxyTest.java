package hello.proxy.jdkdynamic;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Proxy;

import hello.proxy.jdkdynamic.code.AImpl;
import hello.proxy.jdkdynamic.code.AInterface;
import hello.proxy.jdkdynamic.code.BImpl;
import hello.proxy.jdkdynamic.code.BInterface;
import hello.proxy.jdkdynamic.code.TimeInvocationHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdkDynamicProxyTest {
    
    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        
        /*
         * 프록시 사용
         * 파라미터로 넘겨주는 값
         * 클래스로더를 생성(어느클래스로 갈지 지정), 클래스를 배열로 넣어줌(어떤 클래스를 기반으로 인터페이스를 생성할지), handler(프록시가 호출할 로직)
         */
        AInterface proxy = (AInterface)Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        
        proxy.call(); // 프록시를 호출하면 invoke를 실행함
        log.info("targetClass={}", target.getClass());
        log.info("proxyclass={}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        BInterface proxy = (BInterface)Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);
        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyclass={}", proxy.getClass());
    }

    /*
     * 실행순서
     * 1. 클라이언트는 JDK 동적 프록시의 call() 을 실행한다.
        2. JDK 동적 프록시는 InvocationHandler.invoke() 를 호출한다. TimeInvocationHandler 가 구현체로 있으로 TimeInvocationHandler.invoke() 가 호출된다.
        3. TimeInvocationHandler 가 내부 로직을 수행하고, method.invoke(target, args) 를 호출해서 target 인 실제 객체( AImpl )를 호출한다.
        4. AImpl 인스턴스의 call() 이 실행된다.
        5. AImpl 인스턴스의 call() 의 실행이 끝나면 TimeInvocationHandler 로 응답이 돌아온다. 시간 로그를 출력하고 결과를 반환한다.
     */
}
