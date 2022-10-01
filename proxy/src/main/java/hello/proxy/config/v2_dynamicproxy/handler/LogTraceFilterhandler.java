package hello.proxy.config.v2_dynamicproxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.util.PatternMatchUtils;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class LogTraceFilterhandler implements InvocationHandler{

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns; // 해당 패턴일때만 로그를 남김

    public LogTraceFilterhandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메서드 이름 필터
        String methodName = method.getName();
        // 패턴 매칭 
        /*
         * 스프링이 제공하는 PatternMatchUtils.simpleMatch(..) 를 사용하면 단순한 매칭 로직을 쉽게 적용할 수 있다.
            xxx : xxx가 정확히 매칭되면 참 xxx* : xxx로 시작하면 참
            *xxx : xxx로 끝나면 참
            *xxx* : xxx가 있으면 참
         */
        if( !PatternMatchUtils.simpleMatch(patterns, methodName)) {
            // 매칭이 안되면 실제를 호출해줌
            return method.invoke(target, args);
        }

        TraceStatus status = null;

        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);     
            
            // 로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        } 
        catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
    
}
