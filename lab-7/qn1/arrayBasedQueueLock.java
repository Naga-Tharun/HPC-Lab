import java.util.concurrent.locks.ReentrantLock;

public class arrayBasedQueueLock{
    private final boolean[] queue;
    private int front;
    private int rear;
    private final ReentrantLock lock;

    public arrayBasedQueueLock(int capacity){
        queue = new boolean[capacity];
        front = 0;
        rear = 0;
        lock = new ReentrantLock();
    }

    public void acquire(){
        lock.lock();
        try{
            // used to check current position of thread in the queue
            int mySlot = rear;
            // aquire the lock
            queue[rear] = true;
            rear = (rear + 1) % queue.length;
            lock.unlock();
            
            while(queue[front]!=true || mySlot!=front){
                // Busy-wait loop (spinning)
                Thread.yield();
            }
        }
        catch(Exception e){
            lock.unlock();
            throw e;
        }
    }

    public void release(){
        lock.lock();
        try{
            // release the lock and update the front position
            queue[front] = false;
            front = (front + 1) % queue.length;
        }
        finally{
            // executes before any interruption and unlock the lock
            lock.unlock();
        }
    }
}