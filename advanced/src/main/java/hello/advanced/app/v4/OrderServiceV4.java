package hello.advanced.app.v4;

import org.springframework.stereotype.Service;

import hello.advanced.trace.logTrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {
    
    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        /*
         * 재네릭에서 반환 타입이 필요한데, 반환할 내용이 없으면 Void 타입을 사용하고
         * null을 반환하면 된다, 참고로 재네릭은 기본 타입인 void, int 등을 선언할 수 없다
         */
        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {

            @Override
            protected Void call() {
                orderRepository.save(itemId);
                return null;
            }
            
        };
        template.execute("OrderService.orderItem()");
    }
}
