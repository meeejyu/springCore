package hello.aop.pointcut;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import hello.aop.member.MemberServiceImpl;

/*
 *  execution과 args의 차이점
    execution 은 파라미터 타입이 정확하게 매칭되어야 한다. execution 은 클래스에 선언된 정보를 기반으로 판단한다.
    args 는 부모 타입을 허용한다. args 는 실제 넘어온 파라미터 객체 인스턴스를 보고 판단한다.
 */
public class ArgsTest {
    
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    /*
     * pointcut() : AspectJExpressionPointcut 에 포인트컷은 한번만 지정할 수 있다. 
     * 이번 테스트에서는 테스트를 편리하게 진행하기 위해 포인트컷을 여러번 지정하기 위해 포인트컷 자체를 생성하는 메서드를 만들었다.
     */
    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    @Test
    void args() {
        // hello(String)과 매칭
        assertThat(pointcut("args(String)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args()").matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("args(..)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(*)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(String,..)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /*
     * execution(* *(java.io.Serializable)) : 메서드의 시그니처로 판단 (정적)
     * args(java.io.Serializable) : 런타임에 전달된 인수로 판단 (동적)
     */
    /*
        자바가 기본으로 제공하는 String 은 Object , java.io.Serializable 의 하위 타입이다.
        정적으로 클래스에 선언된 정보만 보고 판단하는 execution(* *(Object)) 는 매칭에 실패한다. 
        동적으로 실제 파라미터로 넘어온 객체 인스턴스로 판단하는 args(Object) 는 매칭에 성공한다. (부모 타입 허용)
     */
    @Test
    void argsVsExecution() {

        // Args
        assertThat(pointcut("args(String)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(java.io.Serializable)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)").matches(helloMethod, MemberServiceImpl.class)).isTrue();

        // Execution
        assertThat(pointcut("execution(* *(String))").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("execution(* *(java.io.Serializable))").matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("execution(* *(Object))").matches(helloMethod, MemberServiceImpl.class)).isFalse();

    }


}
