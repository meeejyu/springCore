package hello.proxy.config.v2_dynamicproxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class LogTraceBasicHandler implements InvocationHandler{

    private final Object target;
    private final LogTrace logTrace;
    
    public LogTraceBasicHandler(Object target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        TraceStatus status = null;
        try {
            // method.getDeclaringClass().getSimpleName() 메타 정보를 다 담고 있어서 정보를 가져올수 있다
            String message = method.getDeclaringClass().getSimpleName() + "."  + method.getName() + "()"; // 컨트롤명과 메서드 정보를 가져옴
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
