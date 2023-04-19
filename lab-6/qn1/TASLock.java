import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock{
    // initialize a lock with false
    private AtomicBoolean lock = new AtomicBoolean(false);

    // aquires the lock
    public void acquireLock(){
        // getAndSet updates the value and returns the past value of lock
        // wait until the lock returns false
        while (lock.getAndSet(true)){
            // wait until lock is aquired
        }
    }

    // releases the lock
    public void releaseLock(){
        // makes the lock variable to false
        lock.set(false);
    }
}