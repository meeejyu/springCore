package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) // 실행될때까지 어노테이션이 살아있음, 동적으로 어노테이션을 읽을수 있다
public @interface ClassAop {
    
}
