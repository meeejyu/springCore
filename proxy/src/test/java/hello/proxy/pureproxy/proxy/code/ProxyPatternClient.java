package hello.proxy.pureproxy.proxy.code;

public class ProxyPatternClient {
    
    private Subject subject;

    // 의존관계주입
    public ProxyPatternClient(Subject subject) {
        this.subject = subject;
    }

    public void execute() {
        subject.operation();
    }

    
}
