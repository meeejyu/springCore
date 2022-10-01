package hello.proxy.pureproxy.concreteproxy.code;

public class ConcreteClient {
    
    private ConcreteLogic concreteLogic; // ConcreteLogic, TimeProxy 모두 주입 가능

    // 의존관계 주입
    public ConcreteClient(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute() {
        concreteLogic.operation();
    }
    
}
