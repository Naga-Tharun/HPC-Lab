import java.util.concurrent.atomic.AtomicBoolean;

public class TTASLock{
    // initialize a lock with false
    private final AtomicBoolean lock = new AtomicBoolean(false);
    private static final int SPIN_LIMIT = 100;

    public void acquireLock(){
        boolean spin = true;

        // wait until the spin set to false
        while (spin){
            // first test if lock can be aquired
            while(lock.get()){
                // spin for a short time
                for(int i=0; i<SPIN_LIMIT; i++){
                    Thread.yield();
                }
            }

            // second test-and-set
            // getAndSet updates the value and returns the past value of lock
            spin = lock.getAndSet(true);
        }
    }

    // releases the lock
    public void releaseLock(){
        // makes the lock variable to false
        lock.set(false);
    }
}