package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/*
 * 필드에 전략을 보관하는 방식
 * 
 * 전략 패턴
 * 변하지 않는 로직을 가지고 있는 템플릿 역할을 함
 * 전략 패턴에서는 이것을 컨텍스트(문맥)이라 한다.
 * 쉽게 말해서 컨텍스트는 크게 변하지 않지만, 그 문맥 속에서 strategy를 통해 일부 전략이 변경된다 생각하면 된다
 * 컨텍스트는 Strategy만 의존하기 떄문에 컨텍스트는 변경할 필요가 없다
 * 
 * 해당 클래스는 선 조립 후 실행방식이다.
 */
@Slf4j
public class ContextV1 {
    
    private Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        strategy.call(); // 위임
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

}