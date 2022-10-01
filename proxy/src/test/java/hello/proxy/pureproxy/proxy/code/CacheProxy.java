package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

// 캐쉬역할
@Slf4j
public class CacheProxy implements Subject{

    private Subject target; // 실제 객체
    private String cacheValue; // 캐쉬 데이터

    // 참조하려면 의존관계 주입이 필요함
    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if(cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
    

}
