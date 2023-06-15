import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class LazySkipList{
    static final int MAX_LEVEL = 16;
    final Node head = new Node(Integer.MIN_VALUE);
    final Node tail = new Node(Integer.MAX_VALUE);

    public LazySkipList(){
        for(int i = 0; i < head.next.length; i++){
            head.next[i] = tail;
        }
    }

    private static final class Node{
        final Lock lock = new ReentrantLock();
        final int item;
        final Node[] next;
        volatile boolean marked = false;
        volatile boolean fullyLinked = false;
        private int topLevel;

        public Node(int key){
            this.item = key;
            next = new Node[MAX_LEVEL + 1];
            topLevel = MAX_LEVEL;
        }

        public Node(int item, int height){
            this.item = item;
            next = new Node[height + 1];
            topLevel = height;
        }

        public void lock(){
            lock.lock();
        }

        public void unlock(){
            lock.unlock();
        }
    }

    int find(int x, Node[] preds, Node[] succs){
        int lFound = -1;
        Node pred = head;
        for(int level = MAX_LEVEL; level >= 0; level--){
            Node curr = pred.next[level];
            while(x > curr.item){
                pred = curr;
                curr = pred.next[level];
            }
            if(lFound == -1 && x == curr.item){
                lFound = level;
            }
            preds[level] = pred;
            succs[level] = curr;
        }
        return lFound;
    }

    boolean add(int x){
        int topLevel = randomLevel();
        Node[] preds = new Node[MAX_LEVEL + 1];
        Node[] succs = new Node[MAX_LEVEL + 1];

        while(true){
            int lFound = find(x, preds, succs);
            if(lFound != -1){
                Node nodeFound = succs[lFound];
                if(!nodeFound.marked){
                    while(!nodeFound.fullyLinked){
                        // wait
                    }
                    return false;
                }
                continue;
            }

            int highestLocked = -1;
            try{
                Node pred, succ;
                boolean valid = true;
                for(int level = 0; valid && (level <= topLevel); level++){
                    pred = preds[level];
                    succ = succs[level];
                    pred.lock();
                    highestLocked = level;
                    valid = !pred.marked && !succ.marked && pred.next[level] == succ;
                }
                if(!valid){
                    continue;
                }

                Node newNode = new Node(x, topLevel);
                for(int level = 0; level <= topLevel; level++){
                    newNode.next[level] = succs[level];
                }
                for(int level = 0; level <= topLevel; level++){
                    preds[level].next[level] = newNode;
                }
                newNode.fullyLinked = true; // successful add linearization point
                return true;
            } 
            finally{
                for(int level = 0; level <= highestLocked; level++){
                    preds[level].unlock();
                }
            }
        }
    }

    boolean remove(int x){
        Node victim = null;
        boolean isMarked = false;
        int topLevel = -1;
        Node[] preds = new Node[MAX_LEVEL + 1];
        Node[] succs = new Node[MAX_LEVEL + 1];

        while(true){
            int lFound = find(x, preds, succs);
            if(lFound != -1){
                victim = succs[lFound];
            }
            if(isMarked || (lFound != -1 && (victim.fullyLinked && victim.topLevel == lFound && !victim.marked))){
                if(!isMarked){
                    topLevel = victim.topLevel;
                    victim.lock();
                    if(victim.marked){
                        victim.unlock();
                        return false;
                    }
                    victim.marked = true;
                    isMarked = true;
                }

                int highestLocked = -1;
                try{
                    Node pred;
                    boolean valid = true;
                    for(int level = 0; valid && (level <= topLevel); level++){
                        pred = preds[level];
                        pred.lock();
                        highestLocked = level;
                        valid = !pred.marked && pred.next[level] == victim;
                    }
                    if(!valid){
                        continue;
                    }

                    for(int level = topLevel; level >= 0; level--){
                        preds[level].next[level] = victim.next[level];
                    }
                    victim.unlock();
                    return true;
                } 
                finally{
                    for(int i = 0; i <= highestLocked; i++){
                        preds[i].unlock();
                    }
                }
            } 
            else{
                return false;
            }
        }
    }

    boolean contains(int x){
        Node[] preds = new Node[MAX_LEVEL + 1];
        Node[] succs = new Node[MAX_LEVEL + 1];
        int lFound = find(x, preds, succs);
        return (lFound != -1 && succs[lFound].fullyLinked && !succs[lFound].marked);
    }

    private int randomLevel(){
        int level = 1;
        while(Math.random() < 0.5 && level < MAX_LEVEL){
            level++;
        }
        return level;
    }
}