import java.util.concurrent.atomic.AtomicReference;

public class MCSQueueLock{
    // used to represent each node in the queue
    private static class QNode{
        // represents the state of the node
        volatile boolean locked = false;
        // link to next node in the queue
        volatile QNode next = null;
    }

    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myNode;

    public MCSQueueLock(){
        // initialize the data of the variables
        tail = new AtomicReference<>(null);
        myNode = new ThreadLocal<QNode>(){
            protected QNode initialValue(){ 
                return new QNode();
            }
        };
    }

    // aquiring the lock
    public void acquire(){
        // getting the node
        QNode node = myNode.get();
        // getting previous node value and updating it with node
        QNode pred = tail.getAndSet(node);
        if(pred != null){
            // updating lock on the node
            node.locked = true;
            pred.next = node;
            // wait till the node is unlocked
            while(node.locked){
                // busy-wait loop
                Thread.yield();
            }
        }
    }

    // releasing the lock
    public void release(){
        // getting the mynode value
        QNode node = myNode.get();
        // checking if next node is present or not
        if(node.next == null){
            // check if tail is equal to node and set it to null
            if(tail.compareAndSet(node, null)){
                return;
            }
            // wait untill the node.next is not null
            while(node.next == null){
                // busy-wait loop
                Thread.yield();
            }
        }
        // update the lock and node.next
        node.next.locked = false;
        // remove the link to node.next by placing null
        node.next = null;
    }
}