import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineList {
    private Node head;

    public FineList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
        head.next.prev = head;
    }

    /* Add an element. */
    public boolean add(int item) {
        int key = item;
        Node pred = head;
        pred.lock();
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    return false;
                }
                Node newNode = new Node(item);
                newNode.prev = pred;
                newNode.next = curr;
                curr.prev = newNode;
                pred.next = newNode;
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    /* Remove an element. */
    public boolean remove(int item) {
        int key = item;
        Node pred = null, curr = null;
        Node nodeToDelete = null;
        Node nextNode = null;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    nodeToDelete = curr;
                    nextNode = curr.next;
                    nextNode.lock();
                    try {
                        pred.next = nextNode;
                        nextNode.prev = pred;
                        return true;
                    } finally {
                        nextNode.unlock();
                    }
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
            if (nodeToDelete != null) {
                nodeToDelete.prev = null;
                nodeToDelete.next = null;
            }
        }
    }

    public boolean contains(int item) {
        int key = item;
        Node pred = null, curr = null;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                return (curr.key == key);
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public void display() {
        Node temp = head.next;
        while (temp.next != null) {
            System.out.print("\t" + temp.key);
            temp = temp.next;
        }
        System.out.println();
    }

    /* Node */
    private class Node {
        int key;
        Node next;
        Node prev;
        Lock lock;

        Node(int item) {
            this.key = item;
            this.lock = new ReentrantLock();
            this.next = null;
            this.prev = null;
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }
    }
}