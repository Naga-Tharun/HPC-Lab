import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class cbst{

    public class Node{
        int key;
        String value;
        Node left;
        Node right;
        final ReentrantLock lock;
    
        public Node(int key, String value){
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.lock = new ReentrantLock();
        }
    }
    
    private Node root;

    public cbst(){
        root = null;
    }

    public boolean contains(int key){
        Node current = root;
        // Check if the current node is null before trying to lock it
        if(current != null){
            current.lock.lock();
            try{
                while(current != null){
                    if(current.key == key){
                        return true;
                    } 
                    else if(current.key < key){
                        Node node = current;
                        current = current.right;
                        if(current != null){
                            current.lock.lock();
                        }
                        node.lock.unlock();
                    } 
                    else{
                        Node node = current;
                        current = current.left;
                        if(current != null){
                            current.lock.lock();
                        }
                        node.lock.unlock();
                    }
                }
            } 
            finally{
                if(current != null){
                    current.lock.unlock();
                }
            }
        }
        return false;
    }  

    public boolean insert(int key, String value){
        Node newNode = new Node(key, value);
        if(root == null){
            root = newNode;
            return true;
        } 
        else{
            Node current = root;
            Node parent = null;
            while(true){
                if(parent != null && parent.lock.isHeldByCurrentThread()){
                    parent.lock.unlock();
                }
                parent = current;
                parent.lock.lock(); // Lock the parent node
                if(key < current.key){
                    current = current.left;
                    if(current == null){
                        parent.left = newNode;
                        parent.lock.unlock(); // Unlock the parent node
                        return true;
                    }
                } 
                else{
                    current = current.right;
                    if(current == null){
                        parent.right = newNode;
                        parent.lock.unlock(); // Unlock the parent node
                        return true;
                    }
                }
            }
        }
    } 

    public boolean remove(int key){
        Node parent = null;
        Node current = root;
        boolean isLeftChild = false;
    
        try{
            // Search for the node
            while(current != null && current.key != key){
                if(parent != null && parent.lock.isHeldByCurrentThread()){
                    parent.lock.unlock();
                }
                parent = current;
                parent.lock.lock();
                if(key < current.key){
                    isLeftChild = true;
                    current = current.left;
                } 
                else{
                    isLeftChild = false;
                    current = current.right;
                }
            }
        
            if(current == null){
                if(parent != null && parent.lock.isHeldByCurrentThread()){
                    parent.lock.unlock();
                }
                return false;
            }
        
            // Case 1: Node with no children
            if(current.left == null && current.right == null){
                if(current == root){
                    root = null;
                } 
                else if(isLeftChild){
                    parent.left = null;
                } 
                else{
                    parent.right = null;
                }
            }
            // Case 2: Node with one child
            else if(current.right == null){
                if(current == root){
                    root = current.left;
                } 
                else if(isLeftChild){
                    parent.left = current.left;
                } 
                else{
                    parent.right = current.left;
                }
            } 
            else if(current.left == null){
                if(current == root){
                    root = current.right;
                } 
                else if(isLeftChild){
                    parent.left = current.right;
                } 
                else{
                    parent.right = current.right;
                }
            }
            // Case 3: Node with two children
            else{
                Node successor = getSuccessor(current);
                if(current == root){
                    root = successor;
                }
                else if(isLeftChild){
                    parent.left = successor;
                } 
                else{
                    parent.right = successor;
                }
                successor.left = current.left;
            }

            if(current.lock.isHeldByCurrentThread()){
                current.lock.unlock();
            }
            if(parent != null && parent.lock.isHeldByCurrentThread()){
                parent.lock.unlock();
            }
            return true;
        } 
        finally{
            if(current != null && current.lock.isHeldByCurrentThread()){
                current.lock.unlock();
            }
            if(parent != null && parent.lock.isHeldByCurrentThread()){
                parent.lock.unlock();
            }
        }
    }
    
    private Node getSuccessor(Node delNode){
        Node successorParent = null;
        Node successor = delNode;
        Node current = delNode.right;
    
        while(current != null){
            successorParent = successor;
            successor = current;
            current = current.left;
        }
    
        if(successor != delNode.right){
            successorParent.left = successor.right;
            successor.right = delNode.right;
        }
    
        return successor;
    }
    
}