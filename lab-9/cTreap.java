import java.util.concurrent.locks.ReentrantLock;

public class cTreap{

    class Node{
        final int key;
        final int priority;
        volatile boolean transit1, transit2, mark;
        volatile Node left, right, parent, pred, succ;
        ReentrantLock succLock, treeLock;
    
        Node(int key, int priority){
            this.key = key;
            this.priority = priority;
            this.transit1 = true;
            this.transit2 = false;
            this.mark = false;
            this.left = null;
            this.right = null;
            this.parent = null;
            this.pred = null;
            this.succ = null;
            this.succLock = new ReentrantLock();
            this.treeLock = new ReentrantLock();
        }
    
        boolean trySuccLock(){
            return this.succLock.tryLock();
        }
    
        boolean tryTreeLock(){
            return this.treeLock.tryLock();
        }
    
        void unLock(Node... nodes){
            for(Node node : nodes){
                if(node.treeLock.isHeldByCurrentThread()){
                    node.treeLock.unlock();
                }
                if(node.succLock.isHeldByCurrentThread()){
                    node.succLock.unlock();
                }
            }
        }
    }

    private Node root;

    private Node search(int key){
        Node node = null;
        Node next = root;
        
        while(next != null){
            next.treeLock.lock();
            
            if(node != null){
                node.treeLock.unlock();
            }

            int cmp;
            if(key > next.key){
                cmp = 1;
            }
            else if(key < next.key){
                cmp = -1;
            }
            else{
                cmp = 0;
            }
            
            if(cmp == 0 && !next.transit1 && !next.transit2){
                // Found the node and it's valid.
                return next;
            } 
            else if(cmp < 0){
                node = next;
                next = next.left;
            } 
            else{
                node = next;
                next = next.right;
            }
        }
        
        if(node != null){
            node.treeLock.unlock();
        }
        
        return null;
    }
    

    private boolean insertIntoTreap(Node p, Node tnode, int x, int pri){
        Node s = p.succ;
    
        if((x < tnode.key && tnode.left == null) || (x > tnode.key && tnode.right == null)){
            Node newTNode = new Node(x, pri);
            newTNode.parent = tnode;
            newTNode.succ = s;
            newTNode.pred = p;
            s.pred = newTNode;
            p.succ = newTNode;
    
            if(x < tnode.key){
                tnode.left = newTNode;
            }
            else{
                tnode.right = newTNode;
            }
    
            p.unLock(p, tnode);
            adjustTreap(newTNode);
            return true;
        }

        return false;
    }

    private void adjustTreap(Node tnode){
        while(true){
            Node tnParentParent = tnode.parent.parent;
    
            if(!tnParentParent.tryTreeLock()){
                continue;
            }
            Node tnParent = tnode.parent;
    
            if(tnParentParent.mark || !tnParent.tryTreeLock()){
                tnParentParent.unLock(tnParentParent);
                continue;
            }
    
            if(tnParent.transit1 || tnParent.transit2 || !tnode.tryTreeLock()){
                tnParentParent.unLock(tnParentParent, tnParent);
                continue;
            }
    
            if(tnParentParent != tnode.parent.parent || tnParent != tnode.parent){
                tnParentParent.unLock(tnParentParent, tnParent, tnode);
                continue;
            }
    
            if(tnParent.priority <= tnode.priority){
                boolean flag = false;
    
                if(tnParent.left == tnode){
                    if(tnode.right != null){
                        Node tnRight = tnode.right;
                        Node tnRightParent = tnode.right.parent;
    
                        if(!tnRight.tryTreeLock()){
                            tnParentParent.unLock(tnParentParent, tnParent, tnode);
                            continue;
                        }
    
                        if(tnRight != tnode.right || tnRight.mark || tnRightParent != tnode){
                            tnParentParent.unLock(tnParentParent, tnParent, tnode, tnRight);
                            continue;
                        }
    
                        flag = true;
                    }
    
                    singleRotateRight(tnParent, tnode);
                    if(flag){
                        tnParent.left.unLock(tnParent.left);
                    }
                } 
                else if(tnParent.right == tnode){
                    if(tnode.left != null){
                        Node tnLeft = tnode.left;
                        Node tnLeftParent = tnode.left.parent;
    
                        if(!tnLeft.tryTreeLock()){
                            tnParentParent.unLock(tnParentParent, tnParent, tnode);
                            continue;
                        }
    
                        if(tnLeft != tnode.left || tnLeft.mark || tnLeftParent != tnode){
                            tnParentParent.unLock(tnParentParent, tnParent, tnode, tnLeft);
                            continue;
                        }
    
                        flag = true;
                    }
    
                    singleRotateLeft(tnParent, tnode);
                    if(flag){
                        tnParent.right.unLock(tnParent.right);
                    }
                }
                tnParentParent.unLock(tnParentParent, tnParent, tnode);
            } 
            else{
                tnode.transit1 = false;
                tnParentParent.unLock(tnParentParent, tnParent, tnode);
                break;
            }
        }
    }
    
    boolean contains(int x){
        Node tnode = search(x);
        if(tnode == null){ 
            return false;
        }
        if(tnode.key != x){
            while(tnode.key> x){
                tnode = tnode.pred;
                Thread.yield();
            }

            while(tnode.key < x){
                tnode = tnode.succ;
                Thread.yield();
            }
        }

        return (tnode.key == x) && !tnode.transit2;
    }

    boolean insert(int x, int pri){
        while(true){
            Node tnode = search(x);
            if(tnode == null){
                Node newNode = new Node(x, pri);
                root = newNode;
                return true;
            }
            Node parent = tnode.parent;
            
            Node p;
            if(tnode.key >= x){
                p = tnode.pred;
            }
            else{
                p = tnode;
            }
            
            if(!p.trySuccLock()){
                Thread.yield();
                continue;
            }

            Node s = p.succ;
            if(p.transit2 || s.mark){
                p.unLock(p);
                continue;
            }

            if((x > p.key) && (x <= s.key)){
                if(x == s.key){
                    p.unLock(p);
                    return false;
                }
                if(!tnode.tryTreeLock()){
                    Thread.yield();
                    p.unLock(p);
                    continue;
                }
                if(tnode.transit1 || tnode.transit2 || parent != tnode.parent){
                    p.unLock(p, tnode);
                    continue;
                }

                boolean flag = insertIntoTreap(p, tnode, x, pri);
                if(flag){
                    return true;
                }
                else{
                    p.unLock(p, tnode);
                    Thread.yield();
                }
            }
            p.unLock(p);
            Thread.yield();
        }
    }

    boolean delete(int x){
        while(true){
            Node tnode = search(x);
            if(tnode == null){
                return true;
            }
            Node p;

            if(tnode.key == x){
                p = tnode.pred;
            }
            else{
                p = tnode;
            }
    
            if(!p.trySuccLock()){
                continue;
            }
    
            if(p.mark){
                p.unLock(p);
                continue;
            }
    
            Node s = p.succ;
    
            if(x > p.key && x <= s.key){
                if(s.key > x){
                    p.unLock(p);
                    return false;
                }
    
                if(!s.trySuccLock()){
                    p.unLock(p);
                    continue;
                }
    
                s.transit2 = true;
                tnode = s;
                boolean flag = acquireTreeLocks(tnode);
    
                while(flag){
                    if(tnode.left != null && tnode.right != null){
                        Node child = maxPriorityNode(tnode.left, tnode.right);
                        Node tnodeChild = null;
    
                        if(child == tnode.left && child.right != null){
                            tnodeChild = child.right;
                        }
                        else if(child == tnode.right && child.left != null){
                            tnodeChild = child.left;
                        }
    
                        if(child == tnode.left){
                            singleRotateRight(tnode, tnode.left);
                        }
                        else{                            
                            singleRotateLeft(tnode, tnode.right);
                        }
    
                        if(tnodeChild != null){
                            tnodeChild.unLock(tnodeChild);
                        }
    
                        tnode.parent.parent.unLock(tnode.parent.parent, tnode.parent, tnode);
                        flag = acquireTreeLocks(tnode);
                    } 
                    else{
                        tnode.mark = true;
                        Node sSucc = s.succ;
                        sSucc.pred = p;
                        p.succ = sSucc;
                        p.unLock(p, s);
                        removeFromTreap(tnode);
                        return true;
                    }
                }
    
                if(!flag){
                    p.unLock(p, s);
                    continue;
                }
            }
            p.unLock(p);
        }
    }
    
    private Node maxPriorityNode(Node left, Node right) {
        if(left == null){
            return right;
        } 
        else if(right == null){
            return left;
        } 
        else{
            if(left.priority > right.priority){
                return left;
            }
            return right;
        }
    }

    private void singleRotateRight(Node parent, Node child){
        Node tnParentParent = null;
    
        parent.treeLock.lock();
        child.treeLock.lock();
    
        // Lock the tnParentParent if it exists
        if(parent.parent != null){
            parent.parent.treeLock.lock();
            tnParentParent = parent.parent;
        }
    
        try{
            // Check if the nodes are marked or in transition
            if(parent.transit1 || parent.transit2 || parent.mark || child.transit1 || child.transit2 || child.mark){
                return;
            }
    
            // Check if the parent's parent has changed
            if(tnParentParent != null && parent.parent != tnParentParent){
                return;
            }
    
            parent.left = child.right;
            if(child.right != null){
                child.right.parent = parent;
            }
            child.right = parent;
            child.parent = parent.parent;

            if(parent.parent != null){
                if(parent.parent.left == parent){
                    parent.parent.left = child;
                } 
                else{
                    parent.parent.right = child;
                }
                // Unlock the tnParentParent
                parent.parent.treeLock.unlock();
            }
            parent.parent = child;
    
        } 
        finally{
            parent.treeLock.unlock();
            child.treeLock.unlock();
        }
    }
     
    private void singleRotateLeft(Node parent, Node child){
        Node tnParentParent = null;
    
        parent.treeLock.lock();
        child.treeLock.lock();
    
        // Lock the tnParentParent if it exists
        if(parent.parent != null){
            parent.parent.treeLock.lock();
            tnParentParent = parent.parent;
        }
    
        try {
            // Check if the nodes are marked or in transition
            if(parent.transit1 || parent.transit2 || parent.mark || child.transit1 || child.transit2 || child.mark){
                return;
            }
    
            // Check if the parent's parent has changed
            if(tnParentParent != null && parent.parent != tnParentParent){
                return;
            }
    
            parent.right = child.left;
            if(child.left != null){
                child.left.parent = parent;
            } 
            child.left = parent;
            child.parent = parent.parent;

            if(parent.parent != null){
                if(parent.parent.right == parent){
                    parent.parent.right = child;
                } 
                else{
                    parent.parent.left = child;
                }
                // Unlock the tnParentParent
                parent.parent.treeLock.unlock();
            }
            parent.parent = child;
        } 
        finally{
            parent.treeLock.unlock();
            child.treeLock.unlock();
        }
    }
    
    private boolean acquireTreeLocks(Node tnode){
        Node parent, tnParentParent;
        Node tnParentParentPrev = null;

        do{
            parent = tnode.parent;
            if(parent == null){
                return true; // Root node, no parent or tnParentParent to lock
            }
    
            tnParentParent = parent.parent;
            if(tnParentParent != null){
                try{
                    if(!tnParentParent.treeLock.tryLock()){
                        return false;
                    }
                } 
                catch(Exception e){
                    return false;
                }
    
                // Check if the tnParentParent has changed or if it's marked or in transition
                if(tnParentParentPrev != null && tnParentParent != tnParentParentPrev || tnParentParent.transit1 || tnParentParent.transit2 || tnParentParent.mark){
                    tnParentParent.treeLock.unlock();
                    return false;
                }
                tnParentParentPrev = tnParentParent;
            }
    
            try{
                if(!parent.treeLock.tryLock()){
                    if(tnParentParent != null){
                        tnParentParent.treeLock.unlock();
                    }
                    return false;
                }
            } 
            catch(Exception e){
                if(tnParentParent != null){
                    tnParentParent.treeLock.unlock();
                }
                return false;
            }
    
            // Check if the parent is marked or in transition
            if(parent.transit1 || parent.transit2 || parent.mark){
                parent.treeLock.unlock();
                if(tnParentParent != null){
                    tnParentParent.treeLock.unlock();
                }
                return false;
            }
    
        } while(parent != tnode.parent); // check if parent changed during locking
    
        return true;
    }
    
    private void removeFromTreap(Node tnode){
        Node parent = tnode.parent;

        if(parent == null){
            // If the node to be removed is root, just set root to null
            if(root.treeLock.tryLock()){
                try{
                    if(tnode == root){
                        root = null;
                    }
                } 
                finally{
                    root.treeLock.unlock();
                }
            }
        }
        else{
            // Acquire lock on parent
            if(parent.treeLock.tryLock()){
                try{
                    // Check if parent is marked or in transition
                    if(parent.transit1 || parent.transit2 || parent.mark){
                        return;
                    }
    
                    // Check if parent is still the parent of tnode
                    if(tnode.parent != parent){
                        return;
                    }
    
                    if(tnode == parent.left){
                        // If the node is the left child of its parent, set parent's left child to null
                        parent.left = null;
                    } 
                    else if(tnode == parent.right){
                        // If the node is the right child of its parent, set parent's right child to null
                        parent.right = null;
                    }
                } 
                finally{
                    // Release lock on parent
                    parent.treeLock.unlock();
                }
            }
        }
    }

}