package hello.aop.proxyvs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @SpringBootTest(properties =  {"spring.aop.proxy-target-class=false"}) // JDK 동적 프록시, DI 예외 발생
/*
 *  @Autowired MemberService memberService : 이 부분은 문제가 없다. 
 * JDK Proxy는 MemberService 인터페이스를 기반으로 만들어진다. 따라서 해당 타입으로 캐스팅 할 수 있다.
        MemberService = JDK Proxy 가 성립한다.  
    @Autowired MemberServiceImpl memberServiceImpl : 문제는 여기다. JDK Proxy는
    MemberService 인터페이스를 기반으로 만들어진다. 따라서 MemberServiceImpl 타입이 뭔지 전혀 모른다. 그래서 해당 타입에 주입할 수 없다.
        MemberServiceImpl = JDK Proxy 가 성립하지 않는다.
 */

@SpringBootTest(properties =  {"spring.aop.proxy-target-class=true"}) // CGLIB 프록시, 성공
/*
 * 
@Autowired MemberService memberService : CGLIB Proxy는 MemberServiceImpl 구체 클래스를 기반으로 만들어진다. 
MemberServiceImpl 은 MemberService 인터페이스를 구현했기 때문에 해당 타입으로 캐스팅 할 수 있다.
    MemberService = CGLIB Proxy 가 성립한다.
@Autowired MemberServiceImpl memberServiceImpl : CGLIB Proxy는 MemberServiceImpl
구체 클래스를 기반으로 만들어진다. 따라서 해당 타입으로 캐스팅 할 수 있다. 
    MemberServiceImpl = CGLIB Proxy 가 성립한다.
 */
@Import(ProxyDIAspect.class)
public class ProxyDITest {
    
    @Autowired
    MemberService memberService; // JDK 동적 프록시 OK, CGLIB OK

    @Autowired
    MemberServiceImpl memberServiceImpl; // JDK 동적 프록시 X, CGLIB OK

    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());

        memberServiceImpl.hello("hello");
    }

    /*
     * 정리
     * JDK 동적 프록시는 대상 객체인 MemberServiceImpl 타입에 의존관계를 주입할 수 없다. 
     * CGLIB 프록시는 대상 객체인 MemberServiceImpl 타입에 의존관계 주입을 할 수 있다.
     */

}
