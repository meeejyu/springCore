package hello.advanced.trace.logTrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) { // 로그 시작
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("["+ traceId.getId() +"]" + addSpace(START_PREFIX, traceId.getLevel()) + message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) { 
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) { 
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if(e == null) {
            log.info("["+ traceId.getId() +"]" + addSpace(COMPLETE_PREFIX, traceId.getLevel()) + 
                status.getMessage() + " time=" + resultTimeMs + "ms");
        }
        else {
            log.info("["+ traceId.getId() +"]" + addSpace(EX_PREFIX, traceId.getLevel()) + 
                status.getMessage() + " time=" + resultTimeMs + "ms" + " ex=" + e.toString());
        }

        releaseTraceId();
    }

    private void syncTraceId() {

        TraceId traceId = traceIdHolder.get();
        if(traceId == null) {
            traceIdHolder.set(new TraceId());
        }
        else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    // 쓰레드를 다 쓰면 쓰레드에 들어있는 값을 반드시 삭제해줘야함
    private void releaseTraceId() {

        TraceId traceId = traceIdHolder.get();

        if(traceId.isFirstLevel()) {
            traceIdHolder.remove(); // 본인 쓰레드만 삭제
        }
        else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level -1) ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }
    

}
