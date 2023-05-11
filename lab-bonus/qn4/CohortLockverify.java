import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CohortLockverify{
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args){
        int threads_count = Integer.parseInt(args[0]);
        // duration is the amount of time the code runs taken as command line argument
        // taken in milliseconds
        int duration = Integer.parseInt(args[1]);
        CohortLock lock = new CohortLock(threads_count);
        List<Thread> threads = new ArrayList<>();

        long start_time = System.currentTimeMillis();
        long end_time = start_time + duration;
        Runnable increment_counter = () -> {
            while(System.currentTimeMillis() < end_time){
                try{
                    lock.lock();
                    counter.incrementAndGet();
                    lock.unlock();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        // starting the threads
        for(int i=0; i<threads_count; i++){
            Thread thread = new Thread(increment_counter);
            thread.start();
            threads.add(thread);
        }

        // trying to run the threads for the given duration
        try{
            // makes the main thread sleep for given duration to enable 
            // worker threads to execute the increment counter task without any interruption
            Thread.sleep(duration);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        // Stopping the threads
        for(Thread thread : threads){
            try{
                thread.join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        int operations = counter.get();
        double throughput = (operations/(1000000.0*duration))*1000;// Millions of Operations per second
        System.out.println("Number of threads: " + threads_count);
        System.out.println("Number of operations: " + operations);
        System.out.println("Elapsed time: " + duration + " ms");
        System.out.println("Throughput: " + throughput);
        System.out.println();
    }
}