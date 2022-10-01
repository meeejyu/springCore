package hello.proxy.jdkdynamic.code;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.InvocationHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {
    
    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // 메소드 호출할떄 target이랑 다른 파라미터 인수가 넘어올수 있으므로 args도 넣어준다.
        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTiem={}", resultTime);
        return result;
    }
    
}
