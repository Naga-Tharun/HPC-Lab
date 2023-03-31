import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazyList {
    private Node head;
    private Node tail;

    public LazyList() {
        // Add sentinels (head and tail)
        this.head = new Node(Integer.MIN_VALUE);
        this.tail = new Node(Integer.MAX_VALUE);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    /* Check that pred and curr are still in list and adjacent */
    private boolean validate(Node pred, Node curr) {
        return !pred.marked && !curr.marked && pred.next == curr && curr.prev == pred;
    }

    /* Add an element. */
    public boolean add(int key) {
        while (true) {
            Node pred = this.head;
            Node curr = this.head.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            try {
                curr.lock();
                try {
                    if (validate(pred, curr)) {
                        if (curr.key == key) { // present
                            return false;
                        } else { // not present
                            Node newNode = new Node(key);
                            newNode.next = curr;
                            newNode.prev = pred;
                            curr.prev = newNode;
                            pred.next = newNode;
                            return true;
                        }
                    }
                } finally {
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    /* Remove an element. */
    public boolean remove(int key) {
        while (true) {
            Node pred = this.head;
            Node curr = this.head.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            try {
                curr.lock();
                try {
                    if (validate(pred, curr)) {
                        if (curr.key != key) { // not present
                            return false;
                        } else { // present
                            curr.marked = true; // logically remove
                            curr.next.prev = pred;
                            pred.next = curr.next; // physically remove
                            return true;
                        }
                    }
                } finally {
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    public boolean contains(int key) {
        Node curr = this.head.next;
        while (curr != this.tail && curr.key <= key) {
            if (curr.key == key && !curr.marked) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public void display() {
        Node temp = this.head.next;
        while (temp != this.tail) {
            if (!temp.marked) {
                System.out.print("\t" + temp.key);
            }
            temp = temp.next;
        }
    }

    /* List Node */
    private class Node {
        int key;
        Node next;
        Node prev;
        boolean marked;
        Lock lock;

        /* Constructor for usual Node*/
        Node(int key) {
            this.key = key;
            this.next = null;
            this.prev = null;
            this.marked = false;
            this.lock = new ReentrantLock();
        }

        /* Lock Node */
        void lock() {
            lock.lock();
        }

        /* Unlock Node */
        void unlock() {
            lock.unlock();
        }
    }
}
