import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class QueueLockWithTimeout implements Lock {
    static QNode AVAILABLE = new QNode();
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myNode;

    public QueueLockWithTimeout() {
        tail = new AtomicReference<QNode>(null);
        myNode = new ThreadLocal<QNode>(){
            protected QNode intitalValue(){
                return new QNode();
            }
        };
    }

    static class QNode {
        public volatile QNode pred = null;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long startTime = System.nanoTime();
        long patience = unit.toNanos(time);
        QNode qnode = new QNode();
        myNode.set(qnode);
        qnode.pred = null;
        QNode myPred = tail.getAndSet(qnode);

        if (myPred == null || myPred.pred == AVAILABLE) {
            return true;
        }

        while(System.currentTimeMillis() - startTime < patience){
            QNode predPred = myPred.pred;
            if(predPred == AVAILABLE){
                return true;
            }
            else if(predPred != null){
                myPred = predPred;
            }
        }

        if(!tail.compareAndSet(qnode, myPred)){
            qnode.pred = myPred;
        }

        return false;
    }

    public void unlock() {
        QNode qnode = myNode.get();
        if (!tail.compareAndSet(qnode, null)) {
            qnode.pred = AVAILABLE;
        }
    }

    // Unimplemented Lock interface methods

    @Override
    public void lock() {
        try {
            tryLock(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("lockInterruptibly is not supported by this lock implementation.");
    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(0, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Conditions are not supported by this lock implementation.");
    }
}
