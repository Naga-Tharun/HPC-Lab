import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TTASBackoffverify{

    private static int NUM_THREADS = 0;
    private static TTASBackoffLock lock = new TTASBackoffLock();
    static long counter = 0;

    public static void main(String[] args) throws InterruptedException{
        NUM_THREADS = Integer.parseInt(args[0]);
        // duration is the amount of time the code runs taken as command line argument
        // taken in milliseconds
        int duration = Integer.parseInt(args[1]);

        // executorservice handles the threads and executes the task on multiple threads
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;
        while(System.currentTimeMillis() < endTime){
            executorService.execute(() -> {
                lock.acquireLock();
                counter++;
                lock.releaseLock();
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        double throughput=(counter/(1000000.0*duration))*1000;// Millions of Operations per second
        System.out.println("Number of threads: " + NUM_THREADS);
        System.out.println("Number of operations: " + counter);
        System.out.println("Elapsed time: " + duration + " ms");
        System.out.println("Throughput: " + throughput);
        System.out.println();
    }
}