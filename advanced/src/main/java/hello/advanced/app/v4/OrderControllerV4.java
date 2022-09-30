package hello.advanced.app.v4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.advanced.trace.logTrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {
    
    private final OrderServiceV4 orderService;
    private final LogTrace trace;

    @GetMapping("/v4/request")
    public String request(String itemId) {

        // 제네릭을 String으로 설정했다. 따라서 반환 타입은 String이 된다
        AbstractTemplate<String> template = new AbstractTemplate<String>(trace) {

            // 익명 내부 클래스를 사용한다. 객체를 생성하면서 상속받은 자식 클래스를 정의했다.
            // 따라서 별도의 자식 클래스를 직접 만들지 않아도 된다
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };
        
        return template.execute("OrderController.request()");
    }
}

/*
 * 단일 책임 원칙(SRP)
 * 로그를 남기는 부분에 단일 책임 원칙을 지켜 변경 지점ㅇㄹ 하나로 모아서 변경에 쉽게 대처할 수 있는 구조를 만든 것이다.
 */
