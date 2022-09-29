package hello.advanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.advanced.trace.logTrace.FieldLogTrace;
import hello.advanced.trace.logTrace.LogTrace;
import hello.advanced.trace.logTrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {
    
    // 싱글톤으로 등록됨
    @Bean
    public LogTrace logTrace() {
        // return new FieldLogTrace();
        return new ThreadLocalLogTrace();
    }

    /*
     * 동시성 문제 발생
     * 애플리케이션에 딱 1개 존재하는데 여러 쓰레드가 동시에 접근하기 때문에 문제가 발생함
     * 
     * ->
     * ThreadLocal을 쓰면 해결됨
     * 쓰레드 로컬의 값을 꼭 삭제해줘야함
     * 
     * 어떤 문제가 발새하는가?
     * 쓰레드를 썼다가 삭제 안하고 반환하면 쓰레드를 재활용하기 때문에 저장되있던 데이터가 노출된다
     */


}
