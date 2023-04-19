import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class TTASBackoffLock{
    // initialize a lock with false
    private AtomicBoolean lock = new AtomicBoolean(false);
    // range of delay (min, max)
    private final int min_backoff_delay = 1; // in milliseconds
    private final int max_backoff_delay = 10000; // in milliseconds

    public void acquireLock(){
        // initial backoff is min delay
        int backoffTime = min_backoff_delay;
        while(true){
            while(lock.get()){
                // test-and-test-and-set
                // wait till we get lock to false
            }
            if(!lock.getAndSet(true)){
                return;
            }
            else{
                try{
                    // generating a reandom delay by sleep
                    Random rand = new Random();
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt() % backoffTime);
                    // updating the backoff time exponentially within the max delay
                    backoffTime = Math.min(backoffTime * 2, max_backoff_delay);
                }catch (InterruptedException e){
                    // ignore interruption
                }
            }
        }
    }

    // releases the lock
    public void releaseLock(){
        lock.set(false);
    }
}