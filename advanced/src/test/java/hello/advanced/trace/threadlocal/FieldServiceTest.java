package hello.advanced.trace.threadlocal;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldServiceTest {
    
    private FieldService fieldService = new FieldService();

    @Test
    void field() {

        log.info("main start");

        // 쓰레드를 2개 만듦
        Runnable userA = () -> {
            fieldService.logic("userA");
        };
        /* 
         * Runnable는 아래의 로직과 같은거
         * @Ovrride
         * public void run() {
         * 
         * }
         */

        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread_A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread_B");

        threadA.start();
        sleep(2000); // 동시성 문제 발생X
        // sleep(100);  // 동시성 문제 발생O
        
        threadB.start();
        sleep(3000); // 메인 쓰레드 종료 대기
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

/*
 * 동시성 문제는 지역변수에서는 발생하지 않는다
 * 지역 변수는 쓰레드마다 각각 다른 메모리 영역이 할당된다.
 * 동시성 문제가 발생하는 곳은 같이 인스턴스 필드 또는 static 같은 공용 필드에 접근할 때 발생한다.
 * 
 */
