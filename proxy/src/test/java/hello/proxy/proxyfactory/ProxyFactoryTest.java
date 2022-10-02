package hello.proxy.proxyfactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import hello.proxy.common.advice.TimeAdivice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;;

/*
 * 스프링은 동적 프록시를 통합해서 편리하게 만들어주는 프록시 팩토리라는 기능을 제공한다
 */
@Slf4j
public class ProxyFactoryTest {
    
    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        /*
         * 프록시 팩토리를 생성할떄, 생성자에 프록시 호출 대상을 함께 넘겨준다.
         * 프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다
         * 인터페이스가 있으면 JDK 동적 프록시를 사용하고
         * 인터페이스가 없으면 CGLIB를 통해서 동적 프록시를 생성한다
         */
        ProxyFactory proxyFactory = new ProxyFactory(target); // 타겟을 넣어서 프록시 팩토리를 만들어줌
        proxyFactory.addAdvice(new TimeAdivice()); // 프록시 팩토리를 통해서 만든 프록시가 사용할 부가 기능 로직을 설정한다
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        // AopUtils 프록시 팩토리를 쓸때만 사용할수 있음 
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdivice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("tartgetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵셥을 사용하려면 인터페이스가 있어도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        // 프록시를 만드는데 타겟클래스를 기반으로 만듦. 프록시를 ServiceImpl을 상속받아서 CGLIB를 사용
        // proxyFactory.setProxyTargetClass(true) true를 넣으면 인터페이스가 있어도 강제로 CGLIB를 사용한다
        // 인터페이스가 아닌 클래스 기반의 프록시를 만들어준다
        proxyFactory.setProxyTargetClass(true); // 중요
        proxyFactory.addAdvice(new TimeAdivice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("tartgetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
