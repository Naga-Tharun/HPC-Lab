import java.util.concurrent.TimeUnit;

class Backoff{
    private final int minDelay, maxDelay;
    private int limit;

    public Backoff(int minDelay, int maxDelay){
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        limit = minDelay;
    }

    public void backoff(){
        int delay = (int)(Math.random()*limit);
        limit = Math.min(maxDelay, 2*limit);
        try{
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
