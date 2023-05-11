import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CohortLock implements Lock{
    private final AtomicInteger cohortCounter;
    private final ReentrantLock[] cohortLocks;

    public CohortLock(int numCohorts){
        cohortCounter = new AtomicInteger(0);
        cohortLocks = new ReentrantLock[numCohorts];
        for(int i=0; i<numCohorts; i++){
            cohortLocks[i] = new ReentrantLock();
        }
    }

    @Override
    public void lock(){
        int cohortIndex = cohortCounter.getAndIncrement() % cohortLocks.length;
        cohortLocks[cohortIndex].lock();
    }

    @Override
    public void unlock(){
        for(ReentrantLock cohortLock : cohortLocks){
            if(cohortLock.isHeldByCurrentThread()){
                cohortLock.unlock();
                break;
            }
        }
    }

    // Additional methods from the Lock interface
    @Override
    public void lockInterruptibly() throws InterruptedException{
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean tryLock(){
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException{
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Condition newCondition(){
        throw new UnsupportedOperationException("Not implemented");
    }
}
