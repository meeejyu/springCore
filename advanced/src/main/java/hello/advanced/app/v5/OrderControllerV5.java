package hello.advanced.app.v5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logTrace.LogTrace;

@RestController
public class OrderControllerV5 {
    
    private final OrderServiceV5 orderService;
    private final TraceTemplate template; 

    /*
     * trace 의존관계 주입을 받으면서 필요한 TraceTemplate 템플릿을 생성한다.
     * TraceTemplate 을 처음부터 스프링 빈으로 등록하고 주입받아도 된다.
     */
    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {

        return template.execute("OrderController.request()", new TraceCallback<>() {
            
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        });
    }
}
