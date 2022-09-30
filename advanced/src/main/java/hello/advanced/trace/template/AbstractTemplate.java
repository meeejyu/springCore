package hello.advanced.trace.template;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logTrace.LogTrace;

// 템플릿 메서드 패턴에서 부모 클래스이고 템플릿 역할을 한다
// <T> 제네릭을 사용했다 반환타입을 정의한다, 나중에 반환할 타입을 정할수 있다.
public abstract class AbstractTemplate<T> {
    
    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            // 로직 호출
            T result = call();

            trace.end(status);
            return result;
        } 
        catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
    
    // abstract T call() 변하는 부분을 처리
    protected abstract T call();

}
