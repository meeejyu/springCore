package hello.advanced.trace.threadlocal;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalServiceTest {
    
    private ThreadLocalService service = new ThreadLocalService();

    @Test
    void threadLocal() {
        log.info("main start");
        // 쓰레드를 2개 만듦
        Runnable userA = () -> {
            service.logic("userA");
        };
        Runnable userB = () -> {
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread_A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread_B");

        threadA.start();
        sleep(100);        
        threadB.start();
        sleep(2000); 

        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);    
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
