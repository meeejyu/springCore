package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2{
    
    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {

        // super()은 항상 있지만 적지는 않는다
        // 자식 타입에 기본생성자가 아닌 생성자가 있으면 자식타입의 생성자를 호출해야한다
        super(null); // 안쓸것이기 때문에 null을 넣어줌, 프록시 역할만 함. 프록시는 부모 객체의 기능을 사용하지 않기 때문에 super(null)을 입력
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {

        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderService.orderItem()");
            // target 호출
            target.orderItem(itemId);
            logTrace.end(status);    
        } 
        catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        
    }
    


}
