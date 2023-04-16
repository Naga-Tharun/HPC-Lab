// Naga Tharun Makkena
// SE20UCSE105
public class peterson_lock {
    // volatile makes the instance to be used by multiple threads without any problem
    // flag is used to check the desire of each thread to run
    volatile boolean[] flag;
    // turn acts as an indicator that states which thread can be run
    volatile int turn;

    peterson_lock() {
        // initializing flag array to false
        flag = new boolean[2];
        flag[0] = false;
        flag[1] = false;
        // giving the turn to thread 0 first
        turn = 0;
    }

    // locks the thread 
    public void lock(int id) {
        // find other thread id
        int other_id = 1 - id;
        // change flag to notifyt hat the thread wants to lock
        flag[id] = true;
        // give chance for the other thread id to run
        turn = other_id;
        // wait until the other thread completes its excution and changes its flag
        while (flag[other_id] && turn == other_id) {
            // wait state
        }
    }

    // unlocks the thread
    public void unlock(int id) {
        // release the flag on the given thread
        flag[id] = false;
    }
}
