import java.util.concurrent.atomic.AtomicReference;

public class CLHQueueLock{
    // used to represent each node in the queue
    private static class QNode{
        // represents the state of the node
        volatile boolean locked = false;
    }

    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myNode;
    private final ThreadLocal<QNode> myPred;

    public CLHQueueLock(){
        // initialize the data of the variables
        tail = new AtomicReference<QNode>(new QNode());
        myNode = new ThreadLocal<QNode>(){
            protected QNode initialValue(){ 
                return new QNode();
            }
        };
        myPred = new ThreadLocal<QNode>(){
            protected QNode initialValue(){ 
                return null;
            }
        };
    }

    // aquiring the lock by updating the locked variable of node to true and waiting till prev node is unlocked
    public void acquire(){
        QNode node = myNode.get();
        node.locked = true;
        QNode pred = tail.getAndSet(node);
        myPred.set(pred);
        while(pred.locked){
            // busy-wait loop 
            Thread.yield();
        }
    }

    // releasing the lock by setting locked variable as false and updating myNode value
    public void release(){
        QNode node = myNode.get();
        node.locked = false;
        myNode.set(myPred.get());
    }
}