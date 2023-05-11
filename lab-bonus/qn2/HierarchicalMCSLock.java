import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class HierarchicalMCSLock implements Lock{
    private static final class Node{
        AtomicReference<Node> next;
        boolean locked;

        Node(){
            next = new AtomicReference<>(null);
            locked = true;
        }
    }

    private final ThreadLocal<Node> myNode = ThreadLocal.withInitial(Node::new);
    private final AtomicReference<Node> tail = new AtomicReference<>(null);

    @Override
    public void lock(){
        Node node = myNode.get();
        Node pred = tail.getAndSet(node);
        if(pred != null){
            pred.next.set(node);
            while(node.locked){
                Thread.yield();
            }
        }
    }

    @Override
    public void unlock(){
        Node node = myNode.get();
        if(node.next.get() == null){
            if(tail.compareAndSet(node, null)){
                return;
            }
            while(node.next.get() == null){
                Thread.yield();
            }
        }
        node.next.get().locked = false;
        node.next.set(null);
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
