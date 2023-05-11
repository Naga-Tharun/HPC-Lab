import java.util.concurrent.atomic.AtomicInteger;

public class ThreadID{
    private static final AtomicInteger nextId = new AtomicInteger(0);
    private static final int NUM_CLUSTERS = 16;

    private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue(){
            return nextId.getAndIncrement();
        }
    };

    private static final ThreadLocal<Integer> clusterId = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue(){
            return threadId.get()%NUM_CLUSTERS;
        }
    };

    public static int get(){
        return threadId.get();
    }

    public static int getCluster(){
        return clusterId.get();
    }
}