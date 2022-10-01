package hello.advanced.trace.callback;

/*
 * 콜백을 전달하는 인터페이스이다.
 * 콜백이 반환타입을 정의한다.
 */
public interface TraceCallback<T> {
    T call();
}
