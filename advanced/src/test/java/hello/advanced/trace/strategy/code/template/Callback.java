package hello.advanced.trace.strategy.code.template;

/*
 * 템플릿 콜백 패턴
 *  스프링에서는 ContextV2와 같은 방식의 전략 패턴을 템플릿 콜백 패턴이라 한다.
 *  전략 패턴에서 Context가 템플릿 역할을 하고, Strategy부분이 콜백으로 넘어온다 생각하면 된다.
 *  스프링 내부에서 자주 사용하는 방식
 *  스프링에서 이름이 xxxTemplate가 있다면 템플릿 콜백 패턴이라고 생각하면 된다
 *  ex) JdbcTemplate, RestTemplate
 */
public interface Callback {
    void call();
}
