package hello.proxy.cglib;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CglibTest {
    
    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        // cglib를 만들 코드
        Enhancer enhancer = new Enhancer();
        // 동적 프록시 만들때 ConcreteService를 기반으로 만들어줌, 어떤 구체 클래스를 상속 받을지 지정한다
        enhancer.setSuperclass(ConcreteService.class);
        // 프록시에 적용할 실행 로직을 할당한다
        enhancer.setCallback(new TimeMethodInterceptor(target));
        // 프록시 생성
        ConcreteService proxy = (ConcreteService)enhancer.create();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

    }
}
