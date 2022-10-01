package hello.proxy.config.v1_proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;

/*
 * 프록시를 생성하고 프록시를 실제 스프링 빈 대신 등록한다.
 * 실제 객체는 스프링 빈으로 등록하지 않는다.
 * 스프링 빈으로 실제 객체 대신에 프록시 객체를 등록했기 떄문에 앞으로 스프링 빈을 주입 받으면 실제 객체 대신에 프록시 객체가 주입된다
 */
@Configuration
public class ConcreteProxyConfig {
    
    @Bean
    public OrderControllerV2 orderController(LogTrace logTrace) {
        OrderControllerV2 controllerImpl = new OrderControllerV2(orderService(logTrace)); // target
        return new OrderControllerConcreteProxy(controllerImpl, logTrace); // target이 필요
    }

    @Bean
    public OrderServiceV2 orderService(LogTrace logTrace) {
        
        OrderServiceV2 serviceImpl = new OrderServiceV2(orderRepository(logTrace));
        return new OrderServiceConcreteProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV2 orderRepository(LogTrace logTrace) {
        OrderRepositoryV2 repositoryImpl = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(repositoryImpl, logTrace);
    }
}
