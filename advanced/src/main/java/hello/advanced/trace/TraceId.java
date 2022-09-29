package hello.advanced.trace;

import java.util.UUID;

public class TraceId {
    
    private String id; // 트랜잭션 아이디
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId() { // 아이디는 같고 레벨이 증가
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() { // 아이디는 같고 레벨이 감소
        return new TraceId(id, level -1);
    }

    public boolean isFirstLevel() { // 첫번째 레벨인가를 확인
        return level == 0;
    }

    public String getId() {
        return id;
    }
    
    public int getLevel() {
        return level;
    }

}
