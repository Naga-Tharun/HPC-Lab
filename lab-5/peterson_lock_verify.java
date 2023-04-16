// Naga Tharun Makkena
// SE20UCSE105
public class peterson_lock_verify{
    // a counter to verify the peterson lock being implemented
    static int counter = 0;
    // initializing the locks
    static peterson_lock Lock = new peterson_lock();

    // number of operations happening for increment and decrement
    static int value = 1000000;

    // increases the shared variable based on the thread id provided and locking it and unlocking it
    static public synchronized void increment(int id){
        for(int i=0; i<value; i++){
            Lock.lock(id);
            counter++;
            Lock.unlock(id);
        }
    }

    // reduces the shared variable based on the thread id provided and locking it and unlocking it
    static public synchronized void decrement(int id){
        for(int i=0; i<value; i++){
            Lock.lock(id);
            counter--;
            Lock.unlock(id);
        }
    }

    public static void main(String[] args){
        
        // thread 1 executes increment method
        Thread thread1 = new Thread(() -> increment(0));
        // thread 2 executed decrement method
        Thread thread2 = new Thread(() -> decrement(1));

        // start running the threads
        thread1.start();
        thread2.start();

        // completing the execution of threads
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // printing the status of the locks (working)
        System.out.println("The counter is: " + counter);
        System.out.println("The actual counter is: 0");

        if(counter == 0){
            System.out.println("\nThe lock is working as expected!\n");
        }
        else{
            System.out.println("\nThe lock is not working as expected!\n");
        }
    }
}