import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class HierarchicalBackoffLock implements Lock{
    private static final int LOCAL_MIN_DELAY = 50;
    private static final int LOCAL_MAX_DELAY = 500;
    private static final int REMOTE_MIN_DELAY = 1000;
    private static final int REMOTE_MAX_DELAY = 2000;
    private static final int FREE = -1;
    private AtomicInteger state;

    public HierarchicalBackoffLock(){
        // initialize the data of the variables
        state = new AtomicInteger(FREE);
    }

    @Override
    public void lock(){
        // aquiring the lock
        int myCluster = ThreadID.getCluster();
        Backoff localBackoff = new Backoff(LOCAL_MIN_DELAY, LOCAL_MAX_DELAY);
        Backoff remoteBackoff = new Backoff(REMOTE_MIN_DELAY, REMOTE_MAX_DELAY);

        while(true){
            if(state.compareAndSet(FREE, myCluster)){
                return;
            }
            int lockState = state.get();

            if(lockState == myCluster){
                localBackoff.backoff();
            }
            else{
                remoteBackoff.backoff();
            }
        }
    }

    @Override
    public void unlock(){
        // releasing the lock
        state.set(FREE);
    }

    // Additional methods from the Lock interface
    @Override
    public Condition newCondition(){
        throw new UnsupportedOperationException("Conditions are not supported by this lock implementation.");
    }

    @Override
    public boolean tryLock(){
        throw new UnsupportedOperationException("tryLock is not supported by this lock implementation.");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit){
        throw new UnsupportedOperationException("tryLock with timeout is not supported by this lock implementation.");
    }

    @Override
    public void lockInterruptibly(){
        throw new UnsupportedOperationException("lockInterruptibly is not supported by this lock implementation.");
    }
}